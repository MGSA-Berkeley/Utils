<?php
$file = file_get_contents('./main.html');
if(!$file) echo "error";
echo str_replace("{{cachebust}}", date('Y-m-d-H-i-s'), $file);
?>
