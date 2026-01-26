package com.ruoyi.mall.recommend;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于用户的协同过滤推荐算法工具类 (User-Based Collaborative Filtering)
 * 
 * 算法流程：
 * 1. 构建用户-商品评分矩阵（基于浏览和评论行为）
 * 2. 计算用户之间的余弦相似度
 * 3. 找出与目标用户最相似的N个用户
 * 4. 推荐相似用户喜欢但目标用户未接触的商品
 * 
 * @author ruoyi
 */
public class UserCFRecommender {

    /** 浏览行为权重 */
    private static final double VIEW_WEIGHT = 1.0;

    /** 评论行为基础权重 */
    private static final double COMMENT_BASE_WEIGHT = 2.0;

    /** 默认相似用户数量 */
    private static final int DEFAULT_SIMILAR_USER_COUNT = 10;

    /** 默认推荐商品数量 */
    private static final int DEFAULT_RECOMMEND_COUNT = 10;

    /** 冷启动阈值：用户行为数少于此值时触发冷启动策略 */
    private static final int COLD_START_THRESHOLD = 3;

    /**
     * 用户行为数据
     */
    public static class UserBehavior {
        private Long userId;
        private Long goodsId;
        private double score; // 综合评分

        public UserBehavior(Long userId, Long goodsId, double score) {
            this.userId = userId;
            this.goodsId = goodsId;
            this.score = score;
        }

        public Long getUserId() { return userId; }
        public Long getGoodsId() { return goodsId; }
        public double getScore() { return score; }
    }

    /**
     * 推荐结果
     */
    public static class RecommendResult {
        private Long goodsId;
        private double predictScore; // 预测评分

        public RecommendResult(Long goodsId, double predictScore) {
            this.goodsId = goodsId;
            this.predictScore = predictScore;
        }

        public Long getGoodsId() { return goodsId; }
        public double getPredictScore() { return predictScore; }
    }

    /**
     * 构建用户-商品评分矩阵
     * 
     * @param viewLogs 浏览记录列表 (userId, goodsId)
     * @param comments 评论记录列表 (userId, goodsId, star)
     * @return 用户-商品评分矩阵 Map<userId, Map<goodsId, score>>
     */
    public static Map<Long, Map<Long, Double>> buildUserItemMatrix(
            List<long[]> viewLogs, 
            List<Object[]> comments) {
        
        Map<Long, Map<Long, Double>> matrix = new HashMap<>();

        // 处理浏览记录
        if (viewLogs != null) {
            for (long[] log : viewLogs) {
                Long userId = log[0];
                Long goodsId = log[1];
                
                matrix.computeIfAbsent(userId, k -> new HashMap<>());
                // 浏览行为得分
                matrix.get(userId).merge(goodsId, VIEW_WEIGHT, Double::sum);
            }
        }

        // 处理评论记录（评论权重 = 基础权重 * 评分/5）
        if (comments != null) {
            for (Object[] comment : comments) {
                Long userId = ((Number) comment[0]).longValue();
                Long goodsId = ((Number) comment[1]).longValue();
                Integer star = ((Number) comment[2]).intValue();
                
                matrix.computeIfAbsent(userId, k -> new HashMap<>());
                // 评论行为得分：基础权重 * (评分/5)，评分越高权重越大
                double commentScore = COMMENT_BASE_WEIGHT * (star / 5.0);
                matrix.get(userId).merge(goodsId, commentScore, Double::sum);
            }
        }

        return matrix;
    }

    /**
     * 计算两个用户之间的余弦相似度
     * 
     * cos(A, B) = (A · B) / (||A|| * ||B||)
     * 
     * @param user1Items 用户1的商品评分
     * @param user2Items 用户2的商品评分
     * @return 余弦相似度 [0, 1]
     */
    public static double calculateCosineSimilarity(
            Map<Long, Double> user1Items, 
            Map<Long, Double> user2Items) {
        
        if (user1Items == null || user2Items == null || 
            user1Items.isEmpty() || user2Items.isEmpty()) {
            return 0.0;
        }

        // 找出共同评分的商品
        Set<Long> commonItems = new HashSet<>(user1Items.keySet());
        commonItems.retainAll(user2Items.keySet());

        if (commonItems.isEmpty()) {
            return 0.0;
        }

        // 计算点积
        double dotProduct = 0.0;
        for (Long itemId : commonItems) {
            dotProduct += user1Items.get(itemId) * user2Items.get(itemId);
        }

        // 计算模长
        double norm1 = Math.sqrt(user1Items.values().stream()
                .mapToDouble(v -> v * v).sum());
        double norm2 = Math.sqrt(user2Items.values().stream()
                .mapToDouble(v -> v * v).sum());

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (norm1 * norm2);
    }

    /**
     * 找出与目标用户最相似的N个用户
     * 
     * @param targetUserId 目标用户ID
     * @param matrix 用户-商品评分矩阵
     * @param topN 相似用户数量
     * @return 相似用户列表 Map<userId, similarity>
     */
    public static Map<Long, Double> findSimilarUsers(
            Long targetUserId, 
            Map<Long, Map<Long, Double>> matrix, 
            int topN) {
        
        Map<Long, Double> targetUserItems = matrix.get(targetUserId);
        if (targetUserItems == null || targetUserItems.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, Double> similarities = new HashMap<>();

        for (Map.Entry<Long, Map<Long, Double>> entry : matrix.entrySet()) {
            Long userId = entry.getKey();
            if (userId.equals(targetUserId)) {
                continue; // 跳过自己
            }

            double similarity = calculateCosineSimilarity(targetUserItems, entry.getValue());
            if (similarity > 0) {
                similarities.put(userId, similarity);
            }
        }

        // 按相似度降序排序，取前N个
        return similarities.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        Map.Entry::getValue, 
                        (e1, e2) -> e1, 
                        LinkedHashMap::new));
    }

    /**
     * 生成推荐结果
     * 
     * @param targetUserId 目标用户ID
     * @param matrix 用户-商品评分矩阵
     * @param similarUsers 相似用户及其相似度
     * @param topN 推荐商品数量
     * @return 推荐商品ID列表（按预测评分降序）
     */
    public static List<RecommendResult> generateRecommendations(
            Long targetUserId,
            Map<Long, Map<Long, Double>> matrix,
            Map<Long, Double> similarUsers,
            int topN) {
        
        // 获取目标用户已接触的商品
        Set<Long> targetUserItems = matrix.getOrDefault(targetUserId, Collections.emptyMap()).keySet();

        // 计算候选商品的预测评分
        Map<Long, Double> candidateScores = new HashMap<>();
        Map<Long, Double> similaritySum = new HashMap<>();

        for (Map.Entry<Long, Double> simUser : similarUsers.entrySet()) {
            Long simUserId = simUser.getKey();
            Double similarity = simUser.getValue();
            
            Map<Long, Double> simUserItems = matrix.get(simUserId);
            if (simUserItems == null) continue;

            for (Map.Entry<Long, Double> item : simUserItems.entrySet()) {
                Long goodsId = item.getKey();
                Double score = item.getValue();

                // 只推荐用户未接触过的商品
                if (!targetUserItems.contains(goodsId)) {
                    // 加权评分累加
                    candidateScores.merge(goodsId, similarity * score, Double::sum);
                    similaritySum.merge(goodsId, similarity, Double::sum);
                }
            }
        }

        // 计算最终预测评分（加权平均）
        List<RecommendResult> results = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : candidateScores.entrySet()) {
            Long goodsId = entry.getKey();
            double weightedScore = entry.getValue();
            double simSum = similaritySum.getOrDefault(goodsId, 1.0);
            double predictScore = weightedScore / simSum;
            results.add(new RecommendResult(goodsId, predictScore));
        }

        // 按预测评分降序排序
        results.sort((a, b) -> Double.compare(b.getPredictScore(), a.getPredictScore()));

        return results.stream().limit(topN).collect(Collectors.toList());
    }

    /**
     * 执行完整的推荐流程
     * 
     * @param targetUserId 目标用户ID
     * @param viewLogs 所有用户的浏览记录 [userId, goodsId]
     * @param comments 所有用户的评论记录 [userId, goodsId, star]
     * @param recommendCount 推荐商品数量
     * @return 推荐的商品ID列表
     */
    public static List<Long> recommend(
            Long targetUserId,
            List<long[]> viewLogs,
            List<Object[]> comments,
            int recommendCount) {
        
        // 1. 构建用户-商品评分矩阵
        Map<Long, Map<Long, Double>> matrix = buildUserItemMatrix(viewLogs, comments);

        // 2. 检查是否需要冷启动处理
        Map<Long, Double> targetUserItems = matrix.get(targetUserId);
        if (targetUserItems == null || targetUserItems.size() < COLD_START_THRESHOLD) {
            // 冷启动：返回空列表，由调用方处理热门商品推荐
            return Collections.emptyList();
        }

        // 3. 找出相似用户
        Map<Long, Double> similarUsers = findSimilarUsers(
                targetUserId, matrix, DEFAULT_SIMILAR_USER_COUNT);

        if (similarUsers.isEmpty()) {
            // 没有相似用户，返回空列表
            return Collections.emptyList();
        }

        // 4. 生成推荐结果
        List<RecommendResult> recommendations = generateRecommendations(
                targetUserId, matrix, similarUsers, recommendCount);

        // 5. 提取商品ID
        return recommendations.stream()
                .map(RecommendResult::getGoodsId)
                .collect(Collectors.toList());
    }

    /**
     * 获取冷启动阈值
     */
    public static int getColdStartThreshold() {
        return COLD_START_THRESHOLD;
    }

    /**
     * 获取默认推荐数量
     */
    public static int getDefaultRecommendCount() {
        return DEFAULT_RECOMMEND_COUNT;
    }
}
