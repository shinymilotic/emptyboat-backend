package overcloud.blog.usecase.blog.search;


//@Service
//public class ArticleSyncElasticConsumer {
//
//    private final ArticleSyncDataService articleSyncDataService;
//    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
//
//    public ArticleSyncElasticConsumer(ArticleSyncDataService articleSyncDataService) {
//        this.articleSyncDataService = articleSyncDataService;
//    }
//
//    @KafkaListener(topicPartitions = {@TopicPartition(topic = "postgres.public.articles", partitions = {"0"})
//    }, groupId = "consumer-articles" )
//    public void listen(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        ArticleBinLogValue articleBinLogValue = objectMapper.readValue(consumerRecord.value(), ArticleBinLogValue.class);
//
//        Optional.ofNullable(articleBinLogValue)
//                .filter(article -> StringUtils.hasText(article.getOp()) &&
//                        ("d".equals(article.getOp()) && article.getBefore() != null) ||
//                        ("c".equals(article.getOp()) && article.getAfter() != null) ||
//                        ("u".equals(article.getOp()) && article.getAfter() != null))
//                .orElseThrow(RuntimeException::new);
//
//        switch (articleBinLogValue.getOp()) {
//            case "c" -> {
//                ArticleSyncData articleSyncData = articleBinLogValue.getAfter();
//                articleSyncDataService.createArticle(articleSyncData);
//            }
//
//            case "u" -> {
//                ArticleSyncData articleSyncData = articleBinLogValue.getAfter();
//                articleSyncDataService.updateArticle(articleSyncData);
//            }
//
//            case "d"-> {
//                ArticleSyncData articleSyncData = articleBinLogValue.getBefore();
//                articleSyncDataService.deleteArticle(articleSyncData.getId());
//            }
//        }
//    }
//
//}
