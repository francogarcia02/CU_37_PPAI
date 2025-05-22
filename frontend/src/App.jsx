import { useEffect, useState } from "react";
import Welcome from "./components/Welcome";
import PantallaOrden from "./components/PantallaOrden";
import LogIn from "./components/LogIn";


const App = () => {
  const [mostrarPrincipal, setMostrarPrincipal] = useState(false);

  useEffect(() => {
    const id = setTimeout(() => {
      setMostrarPrincipal(true);
    }, 3000); // Espera 3 segundos

    return () => clearTimeout(id); // Limpieza por si el componente se desmonta
  }, []);

  const [seHaLogueado, setSeHaLogueado] = useState(false);
  const [sesion, setSesion] = useState({
    fechaHoraInicio: null,
    fechaHoraFin: null,
    nombreUsuario: null,
    contraseña: null
  });

  return (
    <div className="w-screen h-screen overflow-hidden flex items-center justify-center bg-[#f4f5f7] relative">

      {mostrarPrincipal ?

        seHaLogueado ?
          <PantallaOrden SESION={sesion} />
          :
          <LogIn setSeHaLogueado={setSeHaLogueado} setSesion={setSesion} />
        
        :
        <Welcome />}

    </div>
  );
};

export default App;
