
import SeccionOpciones from "./seccionOpciones";
import SeccionCierreOI from "./SeccionCierreOI";
import { useState } from "react";


const PantallaOrden = ({ SESION }) => {
    const [selectedOption, setSelectedOption] = useState(null);

    const renderComponent = () => {

        switch (selectedOption) {
            case "Cerrar Orden de Inspección":
                return <SeccionCierreOI onBack={() => setSelectedOption(null)} />;
            default:
                return <SeccionOpciones
                    setSelectedOption={setSelectedOption}
                />;
        }
    };

    return (


        <div className="flex flex-col">
            <div className=" w-full top-4 p-2 z-10 flex flex-row-reverse">
                <div className="w-auto h-auto flex flex-row-reverse bg-cyan-900 bg-opacity-80 backdrop-blur-sm px-4 py-2 rounded-lg shadow-lg">
                    <p className="text-white text-base font-medium">Hola, <span className="text-cyan-300">{SESION.nombreUsuario}</span></p>
                </div>
            </div>
            {renderComponent()}
        </div>

    );
}

export default PantallaOrden;
