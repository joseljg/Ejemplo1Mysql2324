<?php 

include 'conexion.php';
$idProducto =$_POST['idProducto'];
$foto =$_POST['foto'];


// aqui escribimos codigo sql
$query ="INSERT INTO productos_fotos(idProducto,foto) values('$idProducto' ,'$foto') ";
$resultado =mysqli_query($conexion,$query);

if($resultado){
    echo "foto insertada";
}else{
    echo "datos error";
}
mysqli_close($conexion);

?>