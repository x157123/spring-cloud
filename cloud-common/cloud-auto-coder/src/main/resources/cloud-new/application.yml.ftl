server:
  port: 8002
spring:
  datasource:
    url: ${config.url}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
    username: ${config.username}
    password: ${config.password}
    hikari:
      maximumPoolSize: 10
      minimumIdle: 2
      idleTimeout: 600000
      connectionTimeout: 30000
      maxLifetime: 1800000
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl