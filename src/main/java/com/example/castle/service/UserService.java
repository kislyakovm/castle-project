package com.example.castle.service;

import com.example.castle.dto.*;
import com.example.castle.exception.ResourceNotFoundException;
import com.example.castle.exception.UserAlreadyExistsException;
import com.example.castle.model.UserAchievement;
import com.example.castle.model.Castle;
import com.example.castle.model.User;
import com.example.castle.model.VisitPlan;
import com.example.castle.model.enums.UserLevel;
import com.example.castle.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAchievementRepository userAchievementRepository;
    private final CastleRepository castleRepository;
    private final VisitPlanRepository visitPlanRepository;
    private final CommentRepository commentRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public User registerUser(User user) {
        log.debug("Attempting to register new user with username: {}", user.getUsername());

        validateNewUser(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(new Date());
        user.setReputationPoints(0);
        user.setLevel(UserLevel.BEGINNER);

        User savedUser = userRepository.save(user);
        log.info("Successfully registered new user with ID: {}", savedUser.getId());

        return savedUser;
    }

    public User getUserByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public ProfileDTO getProfile(String username) {
        log.debug("Building profile for user: {}", username);
        User user = getUserByUsername(username);

        return ProfileDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .biography(user.getBiography())
                .country(user.getCountry())
                .city(user.getCity())
                .avatarUrl(user.getAvatarUrl())
                .registrationDate(user.getRegistrationDate())
                .reputationPoints(user.getReputationPoints())
                .level(user.getLevel())
                .lastActivityDate(user.getLastActivityDate())
                .totalAchievements(userAchievementRepository.countByUserId(user.getId()))
                .totalPlannedVisits(visitPlanRepository.countByUserId(user.getId()))
                .totalComments(commentRepository.countByUserId(user.getId()))
                .likesReceived(commentRepository.sumLikesByUserId(user.getId()))
                .visitedCastlesCount(castleRepository.countVisitedByUserId(user.getId()))
                .favoriteCastlesCount(castleRepository.countFavoritesByUserId(user.getId()))
                .build();
    }

    @Transactional
    public ProfileDTO updateProfile(String username, ProfileUpdateRequest request) {
        log.debug("Updating profile for user: {}", username);
        User user = getUserByUsername(username);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBiography(request.getBiography());
        user.setCountry(request.getCountry());
        user.setCity(request.getCity());
        user.setLastActivityDate(new Date());

        User savedUser = userRepository.save(user);
        log.info("Successfully updated profile for user: {}", username);

        return getProfile(savedUser.getUsername());
    }

    @Transactional
    public String updateAvatar(String username, MultipartFile file) {
        log.debug("Updating avatar for user: {}", username);
        User user = getUserByUsername(username);

        String avatarUrl = fileStorageService.storeFile(file, "avatars", username);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);

        log.info("Successfully updated avatar for user: {}", username);
        return avatarUrl;
    }

    public List<AchievementDTO> getUserAchievements(String username) {
        log.debug("Fetching achievements for user: {}", username);
        User user = getUserByUsername(username);

        return userAchievementRepository.findByUserId(user.getId()).stream()
                .map(this::mapToAchievementDTO)
                .collect(Collectors.toList());
    }

    public List<CastleDTO> getFavoriteCastles(String username) {
        log.debug("Fetching favorite castles for user: {}", username);
        User user = getUserByUsername(username);

        return castleRepository.findFavoritesByUserId(user.getId()).stream()
                .map(castle -> mapToCastleDTO(castle, user.getId()))
                .collect(Collectors.toList());
    }

    public List<VisitPlanDTO> getUserVisitPlans(String username) {
        log.debug("Fetching visit plans for user: {}", username);
        User user = getUserByUsername(username);

        return visitPlanRepository.findByUserIdOrderByPlannedDateAsc(user.getId()).stream()
                .map(this::mapToVisitPlanDTO)
                .collect(Collectors.toList());
    }

    private void validateNewUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Имя пользователя", user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email", user.getEmail());
        }
    }

    private AchievementDTO mapToAchievementDTO(UserAchievement userAchievement) {
        return AchievementDTO.builder()
                .id(userAchievement.getId())
                .name(userAchievement.getName())
                .description(userAchievement.getDescription())
                .iconUrl(userAchievement.getIconUrl())
                .earnedDate(userAchievement.getEarnedDate())
                .points(userAchievement.getPoints())
                .achievementType(userAchievement.getType().toString())
                .isUnlocked(userAchievement.isUnlocked())
                .progressPercentage(userAchievement.getProgressPercentage())
                .requirements(userAchievement.getRequirements())
                .build();
    }

    private CastleDTO mapToCastleDTO(Castle castle, Long userId) {
        return CastleDTO.builder()
                .id(castle.getId())
                .name(castle.getName())
                .description(castle.getDescription())
                .location(castle.getLocation())
                .imageUrl(castle.getImageUrl())
                .historicalPeriod(castle.getHistoricalPeriod())
                .rating(castle.getAverageRating())
                .visitCount(castle.getVisitCount())
                .isFavorite(true)
                .nextPlannedVisit(visitPlanRepository
                        .findNextVisitDate(userId, castle.getId())
                        .orElse(null))
                .features(castle.getFeatures())
                .openingHours(castle.getOpeningHours())
                .ticketInfo(castle.getTicketInfo())
                .latitude(castle.getLatitude())
                .longitude(castle.getLongitude())
                .build();
    }

    private VisitPlanDTO mapToVisitPlanDTO(VisitPlan plan) {
        return VisitPlanDTO.builder()
                .id(plan.getId())
                .castleId(plan.getCastle().getId())
                .castleName(plan.getCastle().getName())
                .plannedDate(plan.getPlannedDate())
                .status(plan.getStatus().toString())
                .notes(plan.getNotes())
                .numberOfPeople(plan.getNumberOfPeople())
                .specialRequests(plan.getSpecialRequests())
                .isConfirmed(plan.isConfirmed())
                .createdAt(plan.getCreatedAt())
                .build();
    }

    public void addCastleToFavorites(String username, Long castleId) {
        User user = getUserByUsername(username);
        Castle castle = castleRepository.findById(castleId)
                .orElseThrow(() -> new ResourceNotFoundException("Castle not found with id: " + castleId));

        if (user.getFavoriteCastles() == null) {
            user.setFavoriteCastles(new HashSet<>());
        }

        user.getFavoriteCastles().add(castle);
        userRepository.save(user);
    }

    public void removeCastleFromFavorites(String username, Long castleId) {
        User user = getUserByUsername(username);
        Castle castle = castleRepository.findById(castleId)
                .orElseThrow(() -> new ResourceNotFoundException("Castle not found with id: " + castleId));

        if (user.getFavoriteCastles() != null) {
            user.getFavoriteCastles().remove(castle);
            userRepository.save(user);
        }

    }
}