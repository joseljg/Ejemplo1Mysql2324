<?php 

include 'conexion.php';
$idProducto =$_POST['idProducto'];
$nombre =$_POST['nombre'];
$cantidad =$_POST['cantidad'];
$precio =$_POST['precio'];


// aqui escribimos codigo sql
$query ="INSERT INTO productos(idProducto,nombre,cantidad,precio) values('$idProducto' ,'$nombre', '$cantidad', '$precio') ";
$resultado =mysqli_query($conexion,$query);

if($resultado){
    echo "datos insertados";
}else{
    echo "datos error";
}
mysqli_close($conexion);

?>