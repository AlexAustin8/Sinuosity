<?php
$memberName = $_GET["member"];
$lobbyName = $_GET["name"];

$dirName = "data/" . $lobbyName . ".json";

if(file_exists($dirName)) {

    echo "Success: user joined";
    $jsonData = file_get_contents($dirName);
    
    $oldData = json_decode($jsonData);
    array_push($oldData->users, $memberName);
    $newData = json_encode($oldData);
    
    file_put_contents($dirName, $newData);
    
} else {
    
    echo "Error: lobby does not exist";

}


?>