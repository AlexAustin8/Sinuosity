<?php

$name = $_GET["name"];

$dirName = "data/" . $name . ".json";

if(file_exists($dirName)) {
    echo "Error: lobby already exists";
} else {
    
    
    $lobbyNullData = [];
    $lobbyNullData["users"] = [];
    $lobbyNullData["que"] = [];
    $lobbyNullData = json_encode($lobbyNullData);
    $lobbyFile = fopen($dirName, "w");
    file_put_contents($dirName, $lobbyNullData);
    echo "Success: lobby created";
}





?>