var manifestUri;
var ticket;

let player;

var licenseServer = 'https://multidrm.stream.co.jp/drmapi/wv/nagase-staging';

function initApp(url, _ticket, _titleText, _userid, _playback_speed) {
    manifestUri = url;
    ticket = _ticket;
    document.getElementById("title").textContent = _titleText;
    document.getElementById("userid").textContent = "生徒番号" + _userid;

    document.getElementById("playbackSpeed").value = _playback_speed;

    document.getElementById("playbackSpeed").addEventListener("input", changeSpeed, false);
    changeSpeed();

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
    var speed = document.getElementById("playbackSpeed").value;
    document.getElementById("labelText").innerHTML = "再生速度 " + speed + "x";
    player.setPlaybackRate(speed);
}