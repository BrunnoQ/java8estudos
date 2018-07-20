> carregador_nohup.out
#teste
nohup /opt/java/jre1.8.0_144/bin/java -DLOG_PATH=/opt/logs/ -jar -server -Xms1024m -Xmx1024m carregador.jar &
#prd/hml
#nohup /java/jre1.8.0_144/bin/java -DLOG_PATH=/opt/logs/ -jar -server -Xms1024m -Xmx1024m carregador.jar &

ps -ef | grep carregador.jar
echo Processo Iniciado