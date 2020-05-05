#!/bin/bash
SCRIPT_DIR=$(cd $(dirname $0); pwd)
DIR="toshinApp"

if [ ! -d $DIR ];then
	osascript -e 'display notification "初回起動のため必要ファイルをダウンロードしています。しばらくお待ちください。(約40MB)" with title "東進受講.app"'
	mkdir $DIR
	cd $DIR
	curl -OL https://toshin4mac.netlify.app/install/app.zip
    ls .
	unzip ./app.zip
	chmod 777 ./app/app.sh
	rm ./app.zip
	cd ./..
    osascript -e 'display notification "ダウンロードが完了しました。" with title "東進受講.app"'
fi
bash --login $DIR/app/app.sh 