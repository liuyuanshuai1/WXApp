package com.familypoints.backend.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * 家庭码生成工具类
 * 用于生成唯一的家庭码
 */
@Component
public class FamilyCodeGenerator {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成6位随机家庭码
     * @return 家庭码字符串
     */
    public static String generateFamilyCode() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(index));
        }
        return builder.toString();
    }

    /**
     * 计算家庭码过期时间
     * 根据设计文档，家庭码有效期为7天
     * @return 过期时间
     */
    public static LocalDateTime calculateCodeExpiration() {
        return LocalDateTime.now().plusDays(7);
    }
    
    /**
     * 计算家庭码过期时间（别名方法，用于兼容性）
     * @return 过期时间
     */
    public static LocalDateTime calculateExpiryTime() {
        return calculateCodeExpiration();
    }

    /**
     * 检查家庭码是否过期
     * @param expirationTime 过期时间
     * @return 是否过期
     */
    public static boolean isCodeExpired(LocalDateTime expirationTime) {
        return LocalDateTime.now().isAfter(expirationTime);
    }
    
    /**
     * 检查家庭码是否过期（重载方法，接收时间戳）
     * @param expiryTimeMillis 过期时间戳（毫秒）
     * @return 是否过期
     */
    public static boolean isCodeExpired(Long expiryTimeMillis) {
        LocalDateTime expirationTime = java.time.Instant.ofEpochMilli(expiryTimeMillis)
                .atZone(java.time.ZoneOffset.UTC).toLocalDateTime();
        return isCodeExpired(expirationTime);
    }

    /**
     * 获取时间戳，用于唯一性校验
     * @return 时间戳字符串
     */
    public static String getTimestamp() {
        return String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }
}