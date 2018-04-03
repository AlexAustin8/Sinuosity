<?php

function formatMilliseconds($milliseconds) {
   return floor($milliseconds / 60000) . ":" . floor(($milliseconds / 1000) % 60);
}

require '../vendor/autoload.php';

$session = new SpotifyWebAPI\Session(
    'cf7eda43f4ab43e59c74091e4259d9b2',
    '5abfbfa40f6d4060b6fa24a963a67a4d',
    'http://localhost/tuneUp/auth/callback.php'
);



// Request a access token using the code from Spotify
$session->requestAccessToken($_GET['code']);

$accessToken = $session->getAccessToken();
file_put_contents("spotify.txt",$accessToken);


die();

?>