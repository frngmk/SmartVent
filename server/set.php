<?php
$roomid = $_POST['room'];
$ventid = $_POST['vent'];
$setpoint = $_POST['setpoint'];

$logStr = date(DATE_RFC2822);
$logStr .= ": Setting room " . $roomid;
if ($ventid != '')
    $logStr .= ", vent " . $ventid;
$logStr .= " to setpoint " . $setpoint . "\n";
error_log($logStr, 3, "access.log");

$room = Array();
$room['id'] = $roomid;
$room['temp'] = '73';
$room['vents'] = Array();
for ($vid=0; $vid < 3; $vid++) {
    $idStr = (string)$vid;
    $vent = Array();
    $vent['id'] = $idStr;
    if ($ventid == $idStr or $ventid == '')
        $vent['state'] = $setpoint;
    else 
        $vent['state'] = '50';
    $room['vents'][$idStr] = $vent;
}
if ($ventid=='') {
    echo json_encode($room);
}
else {
    echo json_encode($room['vents'][$ventid]);
}
?>
