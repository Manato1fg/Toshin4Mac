#!/bin/bash

#
# アプリの内部で使われているローンチとアップデート用のスクリプト
#

SCRIPT_DIR=$(cd $(dirname $0); pwd)
DIR="toshinApp"

download() {
    mkdir $DIR
	cd $DIR
	curl -OL https://toshin4mac.netlify.app/install/app.zip
    ls .
	unzip ./app.zip
	chmod 777 ./app/app.sh
	rm ./app.zip
    touch "./app/version.txt"
    VERSION=`curl https://toshin4mac.netlify.app/install/version.txt`
    echo $VERSION >> "./app/version.txt"
	cd ./..
}

if [ ! -d $DIR ];then
    open "https://toshin4mac.netlify.app/install/update.html"
	osascript -e 'display notification "初回起動のため必要ファイルをダウンロードしています。しばらくお待ちください。(約40MB)" with title "東進受講.app"'
	download
    osascript -e 'display notification "ダウンロードが完了しました。" with title "東進受講.app"'
else 
    CURRENT_VERSION=`cat $DIR/app/version.txt`
    VERSION=`curl https://toshin4mac.netlify.app/install/version.txt`
    echo $CURRENT_VERSION
    echo $VERSION
    if [ $CURRENT_VERSION -lt $VERSION ]; then
        osascript -e 'display notification "最新バージョンにアップデートしています。しばらくお待ちください。" with title "東進受講.app"'
        rm -rf $DIR
        download
        osascript -e 'display notification "アップデートが完了しました。" with title "東進受講.app"'
    fi
fi

bash --login $DIR/app/app.sh 