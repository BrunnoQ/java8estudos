> socketclient_nohup.out
#teste
nohup /opt/java/jre1.8.0_144/bin/java -DLOG_PATH=/opt/socketclientsinais/logs/ -jar -server -Xms1024m -Xmx1024m socketclientsinais.jar &
#prd/hml
#nohup //java/jre1.8.0_144/bin/java -DLOG_PATH=/opt//socketclientsinais/logs/ -jar -server -Xms1024m -Xmx1024m socketclientsinais.jar &

ps -ef | grep socketclientsinais.jar
echo Processo Iniciado