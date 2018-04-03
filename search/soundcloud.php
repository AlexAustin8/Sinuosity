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

$searchQuery = "look at me";
if(isset($_POST["query"])) {
    $searchQuery = $_POST["query"];
}


$results = file_get_contents("http://api.soundcloud.com/tracks.json?q=$searchQuery&client_id=45c06cc5419304c3b7d6f594db5d9b72");


$results = json_decode($results);



$fullResult = [];


foreach($results as $item) {

    

    
    $builder = [];
    
    
    $uri = $item->uri . "/stream";

    $uri = between($uri, "ks/", "/st");
    $builder["uri"] = urlencode($uri);
    
    $builder["title"] = urlencode($item->title);
    $builder["artist"] = urlencode($item->user->username);
    $builder["duration"] = urlencode(formatMilliseconds($item->duration));
    $builder["artwork_url"] = urlencode($item->artwork_url);
    array_push($fullResult, $builder);
}

$fullResult = json_encode($fullResult);
echo $fullResult;
?>


