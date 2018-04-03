<?php
function sendRequest($type, $query) {
    

$url = 'http://localhost/tuneUp/search/' . $type . '.php';
$data = array('query' => $query);

// use key 'http' even if you send the request to https://...
$options = array(
    'http' => array(
        'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
        'method'  => 'POST',
        'content' => http_build_query($data)
    )
);
$context  = stream_context_create($options);
$result = file_get_contents($url, false, $context);
$result = json_decode($result);
    
    if(count($result) >= 10) {
        $result = array_slice($result, 0, 9, true);
        
    }

return $result;
}


$combinedResult = [];
$combinedResult["spotify"] = sendRequest("spotify", "mac miller");
$combinedResult["soundcloud"] = sendRequest("soundcloud", "mac miller");

echo json_encode($combinedResult);
?>