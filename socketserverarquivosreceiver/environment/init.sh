> socketserversinais_nohup.out
#teste
nohup /opt//java/jre1.8.0_144/bin/java -DLOG_PATH=/opt/socketserversinais/logs/ -jar -server -Xms1024m -Xmx1024m socketserversinais.jar &
#prd/hml
#nohup /java/jre1.8.0_144/bin/java -DLOG_PATH=/opt//socketserversinais/logs/ -jar -server -Xms1024m -Xmx1024m socketserversinais.jar &

ps -ef | grep socketserversinais.jar
echo Processo Iniciado