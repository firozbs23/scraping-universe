java -javaagent:./grafana-opentelemetry-java.jar \
     -Dotel.exporter.otlp.endpoint=https://firozbs23.grafana.net \
     -Dotel.exporter.otlp.headers="Authorization=Bearer glc_eyJvIjoiMTEyNjMxNiIsIm4iOiJzdGFjay05MzM4OTYtb3RscC13cml0ZS1zY3JhcGluZy11bml2ZXJzZSIsImsiOiJLbzd4M0cwV3Y0czkxUGQyeXE5SjBzN0IiLCJtIjp7InIiOiJwcm9kLWV1LXdlc3QtMiJ9fQ==" \
     -Dotel.service.name=ScrapingUniverse_Production \
     -jar target/scraping-universe-0.0.1-SNAPSHOT.jar
