<?php 
include 'conexion.php';


$result= array();
$result['productos'] =array();
$query ="SELECT * FROM productos";
$responce = mysqli_query($conexion,$query);

while($row = mysqli_fetch_array($responce))
{
    $index['idProducto'] =$row['0'];
    $index['nombre'] =$row['1'];
    $index['cantidad'] =$row['2'];
    $index['precio'] =$row['3'];
    

    array_push($result['productos'], $index);

}
$result["exito"]="1";
echo json_encode($result);

?>