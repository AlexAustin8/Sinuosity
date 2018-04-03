<?php


function formatMilliseconds($milliseconds) {
   return floor($milliseconds / 60000) . ":" . floor(($milliseconds / 1000) % 60);
}

require 'vendor/autoload.php';
$api = new SpotifyWebAPI\SpotifyWebAPI();

// Fetch the saved access token from somewhere. A database for example.

$api = new SpotifyWebAPI\SpotifyWebAPI();
$api->setAccessToken(file_get_contents("tokens/spotify.txt"));


$results = $api->search('xxxtentacion', 'track');

echo "<br><br><br>";

foreach($results->tracks->items as $track) {
//    var_dump($track);
//    print("<pre>".print_r($track,true)."</pre>");

    
    $builder = [];
    $builder["service"] = "spotify";
    $builder["uri"] = $track->uri;
    $builder["title"] = $track->name;
    $builder["artist"] = $track->artists[0]->name;
    $builder["duration"] = formatMilliseconds($track->duration_ms);
    $builder["artwork_url"] = $track->album->images[1]->url;
    
    print("<pre>".print_r($builder,true)."</pre>");

    echo "<br><br><br><br>";
}


?>