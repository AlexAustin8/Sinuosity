<?php

function between($string, $start, $end){
    $string = ' ' . $string;
    $ini = strpos($string, $start);
    if ($ini == 0) return '';
    $ini += strlen($start);
    $len = strpos($string, $end, $ini) - $ini;
    return substr($string, $ini, $len);
}

function formatMilliseconds($milliseconds) {
   return floor($milliseconds / 60000) . ":" . floor(($milliseconds / 1000) % 60);
}

require '../vendor/autoload.php';

$searchQuery = "look at me";
if(isset($_POST["query"])) {
    $searchQuery = $_POST["query"];
}

$api = new SpotifyWebAPI\SpotifyWebAPI();

// Fetch the saved access token from somewhere. A database for example.

$api = new SpotifyWebAPI\SpotifyWebAPI();
$api->setAccessToken(file_get_contents("../auth/spotify.txt"));


$results = $api->search($searchQuery, 'track');


$fullResult = [];
foreach($results->tracks->items as $track) {
//    var_dump($track);
//    print("<pre>".print_r($track,true)."</pre>");

    
    $builder = [];
    $uri = $track->uri;
    $uri = explode(":", $uri);
    $builder["uri"] = $uri[2];
    
    $builder["title"] = urlencode($track->name);
    $builder["artist"] = urlencode($track->artists[0]->name);
    $builder["duration"] = urlencode(formatMilliseconds($track->duration_ms));
    $builder["artwork_url"] = urlencode($track->album->images[1]->url);
    
    array_push($fullResult, $builder);

}
$fullResult = json_encode($fullResult);
echo $fullResult;


?>