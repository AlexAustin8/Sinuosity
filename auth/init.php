<?php




require '../vendor/autoload.php';

$session = new SpotifyWebAPI\Session(
    'cf7eda43f4ab43e59c74091e4259d9b2',
    '5abfbfa40f6d4060b6fa24a963a67a4d',
    'http://localhost/tuneUp/auth/callback.php'
);

$options = [
    'scope' => [
        'playlist-read-private',
        'user-read-private',
    ],
];

header('Location: ' . $session->getAuthorizeUrl($options));
die();
?>