<?php 
include 'conexion.php';

$idProducto =$_POST['idProducto'];
$result= array();
$result['productos_fotos'] =array();
$query ="SELECT * FROM productos_fotos WHERE idProducto LIKE '$idProducto' ";
$response = mysqli_query($conexion,$query);
while($row = mysqli_fetch_array($response))
{
    $index['idProducto'] =$row['0'];
    $index['foto'] =$row['1'];
    array_push($result['productos_fotos'], $index);
}
$result["exito"]="1";
echo json_encode($result);

?>