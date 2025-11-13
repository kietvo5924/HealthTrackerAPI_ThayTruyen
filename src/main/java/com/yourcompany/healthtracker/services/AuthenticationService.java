package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.dtos.*;
import com.yourcompany.healthtracker.models.Role;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.UserGoals;
import com.yourcompany.healthtracker.repositories.UserGoalsRepository;
import com.yourcompany.healthtracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserGoalsRepository userGoalsRepository;

    @Transactional
    public String signup(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();

        UserGoals defaultGoals = UserGoals.builder()
                .user(user)
                .build();
        user.setUserGoals(defaultGoals);

        User savedUser = userRepository.save(user);

        String verificationToken = jwtService.generateEmailVerificationToken(savedUser.getUsername());

        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + verificationToken;
        String emailBody = String.format("""
            <h1>Cảm ơn bạn đã đăng ký tài khoản tại Bệnh viện Nhi Đồng II!</h1>
            <p>Vui lòng nhấp vào link sau để xác thực tài khoản của bạn (link có hiệu lực trong 15 phút):</p>
            <a href="%s" style="padding: 10px 15px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;">Xác thực tài khoản</a>
            """, verificationLink);

        emailService.sendVerificationEmail(savedUser.getEmail(), "Xác thực tài khoản", emailBody);
        return "Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt tài khoản.";
    }

    @Transactional
    public String verifyAccount(String token) {
        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User không tồn tại với token này."));

        if (user.isEnabled()) {
            return "Tài khoản này đã được kích hoạt trước đó.";
        }

        if (!jwtService.isTokenValid(token, user)) {
            throw new IllegalStateException("Token không hợp lệ hoặc đã hết hạn.");
        }

        user.setEnabled(true);
        userRepository.save(user);

        return "Tài khoản đã được kích hoạt thành công. Bạn có thể đăng nhập ngay bây giờ.";
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email hoặc mật khẩu không hợp lệ."));

        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public UserResponseDTO getMyProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng."));

        return UserResponseDTO.fromUser(user);
    }

    public void changePassword(ChangePasswordRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Mật khẩu hiện tại không đúng.");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Mật khẩu mới không khớp với mật khẩu xác nhận.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDTO updateMyProfile(UpdateProfileRequest request) {
        User currentUser = getCurrentAuthenticatedUser();

        if (request.getFullName() != null) {
            currentUser.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            currentUser.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDateOfBirth() != null) {
            currentUser.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            currentUser.setAddress(request.getAddress());
        }
        if (request.getMedicalHistory() != null) {
            currentUser.setMedicalHistory(request.getMedicalHistory());
        }
        if (request.getAllergies() != null) {
            currentUser.setAllergies(request.getAllergies());
        }

        User updatedUser = userRepository.save(currentUser);

        return UserResponseDTO.fromUser(updatedUser);
    }

    @Transactional
    public UserResponseDTO updateUserGoals(UserGoalsDTO goalsDTO) {
        User currentUser = getCurrentAuthenticatedUser();

        // Lấy đối tượng UserGoals (nó phải tồn tại do đã tạo lúc signup)
        UserGoals goals = currentUser.getUserGoals();

        // Cẩn thận: Nếu vì lý do nào đó nó null, hãy tạo mới
        if (goals == null) {
            goals = UserGoals.builder().user(currentUser).build();
            currentUser.setUserGoals(goals);
        }

        // Cập nhật các giá trị
        if (goalsDTO.getGoalSteps() != null) {
            goals.setGoalSteps(goalsDTO.getGoalSteps());
        }
        if (goalsDTO.getGoalWater() != null) {
            goals.setGoalWater(goalsDTO.getGoalWater());
        }
        if (goalsDTO.getGoalSleep() != null) {
            goals.setGoalSleep(goalsDTO.getGoalSleep());
        }
        if (goalsDTO.getGoalCaloriesBurnt() != null) {
            goals.setGoalCaloriesBurnt(goalsDTO.getGoalCaloriesBurnt());
        }
        if (goalsDTO.getGoalCaloriesConsumed() != null) {
            goals.setGoalCaloriesConsumed(goalsDTO.getGoalCaloriesConsumed());
        }

        userGoalsRepository.save(goals); // Chỉ cần save 'goals'

        return UserResponseDTO.fromUser(currentUser);
    }

    @Transactional
    public void saveFcmToken(String fcmToken) {
        User currentUser = getCurrentAuthenticatedUser();
        currentUser.setFcmToken(fcmToken);
        userRepository.save(currentUser);
    }

    public User getCurrentAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng."));
    }

    @Transactional
    public UserResponseDTO updateNotificationSettings(NotificationSettingsDTO settingsDTO) {
        User currentUser = getCurrentAuthenticatedUser();
        currentUser.setRemindWater(settingsDTO.isRemindWater());
        currentUser.setRemindSleep(settingsDTO.isRemindSleep());

        User updatedUser = userRepository.save(currentUser);
        return UserResponseDTO.fromUser(updatedUser);
    }
}

