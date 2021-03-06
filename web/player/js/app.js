var manifestUri;
var ticket;

let player;

var licenseServer = 'https://multidrm.stream.co.jp/drmapi/wv/nagase-staging';

function initApp(url, _ticket, _titleText, _userid, _playback_speed/*,_sso_token, _contents_info, _vod_file_path, _validdtm*/) {
    manifestUri = url;
    ticket = _ticket;
    document.getElementById("title").textContent = _titleText;
    document.getElementById("userid").textContent = "生徒番号" + _userid;

    document.getElementById("playbackSpeed").value = _playback_speed;

    document.getElementById("playbackSpeed").addEventListener("input", changeSpeed, false);

    //registViewed(_sso_token, _vod_file_path, _contents_info, _validdtm);

    // Install built-in polyfills to patch browser incompatibilities.
    shaka.polyfill.installAll();

    // Everything looks good!
    initPlayer();
}

function initPlayer() {
    // Create a Player instance.
    var video = document.getElementById('video');
    player = new shaka.Player(video);

    // Attach player to the window to make it easy to access in the JS console.
    window.player = player;

    // Listen for error events.
    player.addEventListener('error', onErrorEvent);

    player.configure({
        drm: {
            servers: { 'com.widevine.alpha': licenseServer }
        }
    });

    player.getNetworkingEngine().registerRequestFilter(function (type, request) {
        // Only add headers to license requests:
        if (type == shaka.net.NetworkingEngine.RequestType.LICENSE) {
            // This is the specific parameter name and value the server wants:
            // Note that all network requests can have multiple URIs (for fallback),
            // and therefore this is an array. But there should only be one license
            // server URI in this tutorial.
            request.uris[0] += '?custom_data='+ticket;
        }
    });

    // Try to load a manifest.
    // This is an asynchronous process.
    player.load(manifestUri).then(function () {
        changeSpeed();
        // This runs if the asynchronous load is successful.
        console.log('The video has now been loaded!');
    }).catch(onError);  // onError is executed if the asynchronous load fails.
}

function onErrorEvent(event) {
    // Extract the shaka.util.Error object from the event.
    onError(event.detail);
}

function onError(error) {
    // Log the error.
    console.error('Error code', error.code, 'object', error);
}

function changeSpeed(e) {
    var video = document.getElementById('video');
    var speed = document.getElementById("playbackSpeed").value;
    document.getElementById("labelText").innerHTML = "再生速度 " + speed + "x";
    video.playbackRate = speed;
}

var isFlipped = false;
function flip() {
    if(!isFlipped) {
        document.getElementById("main").classList.add("flip");
    } else {
        document.getElementById("main").classList.remove("flip");
    }
    isFlipped = !isFlipped;
}

var isExpanded = false;
function switchSize() {
    if (!isExpanded) {
        notificate("escキーで元に戻ります。", window.innerWidth / 2 - 150, window.innerHeight / 2 - 10, 2000);
        document.getElementById("controller").classList.add("vanish");
        //document.getElementById("video").setAttribute("height");
        document.getElementById("video").setAttribute("width", window.innerWidth);
        console.log(window.innerWidth);
        
    }
    isExpanded = true;
}

document.addEventListener('keydown', keydown);

function keydown(e) {
    if (isExpanded && e.keyCode == 27) {
        document.getElementById("controller").classList.remove("vanish");
        //document.getElementById("video").setAttribute("height", "");
        document.getElementById("video").setAttribute("width", "800");
        isExpanded = false;
    }
}

function registViewed(sso_token, vod_file_path, contents_info, validdtm) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'https://pos2.toshin.com/DRM2/DRM25/Webservice/DRMWebService.asmx', true);

    xhr.setRequestHeader('Content-Type', 'application/soap+xml; charset=utf-8');
    //xhr.setRequestHeader('Host', 'pos2.toshin.com');
    xhr.setRequestHeader('SOAPAction', 'http://pos.toshin.com/registViewedContents');

    var xml = `<?xml version="1.0" encoding="utf-8"?>
<soap12:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://www.w3.org/2003/05/soap-envelope">
  <soap12:Body>
    <registViewedContents xmlns="http://pos.toshin.com/">
      <SSO_TOKEN>${sso_token}</SSO_TOKEN>
      <vodfilepath>${vod_file_path}</vodfilepath>
      <contentsinfo>${contents_info}</contentsinfo>
      <validdtm>${validdtm}</validdtm>
    </registViewedContents>
  </soap12:Body>
</soap12:Envelope>`

    xhr.send(xml);

    xhr.onload = function () {
        let responseObj = xhr.response;
        console.log(responseObj.message);
    };
}