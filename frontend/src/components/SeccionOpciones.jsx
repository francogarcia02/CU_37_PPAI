import OnlyRenderInView from "./OnlyRenderInView";
import MotionCard from "./MotionCard";

const SeccionOpciones = ({ setSelectedOption }) => {
    return (
        <OnlyRenderInView>
            <MotionCard>
        <div className="w-auto h-auto flex flex-col bg-cyan-950 rounded-xl text-white p-6">
            <div className="mb-8">
                <h2 className="text-2xl font-bold">Opciones de Orden</h2>
                <p className="text-cyan-200">Seleccione una opción para continuar</p>
            </div>

            <div className="space-y-4">
                <button 
                    className="w-full bg-cyan-800 hover:bg-cyan-700 text-white px-6 py-3 rounded-lg transition-colors duration-200 
                             flex items-center justify-between group"
                    onClick={() => setSelectedOption("Cerrar Orden de Inspección")}
                >
                    <span>Cerrar Orden de Inspección</span>
                    <svg 
                        xmlns="http://www.w3.org/2000/svg" 
                        className="h-5 w-5 opacity-0 group-hover:opacity-100 transition-opacity duration-200" 
                        fill="none" 
                        viewBox="0 0 24 24" 
                        stroke="currentColor"
                    >
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 5l7 7m0 0l-7 7m7-7H3" />
                    </svg>
                </button>

                {/* Ejemplo de otro botón de opción (puedes agregar más según necesites) */}
                <button 
                    className="w-full bg-cyan-800 hover:bg-cyan-700 text-white px-6 py-3 rounded-lg transition-colors duration-200
                             flex items-center justify-between group opacity-50 cursor-not-allowed"
                    disabled
                >
                    <span>Otra opción (Próximamente)</span>
                    <svg 
                        xmlns="http://www.w3.org/2000/svg" 
                        className="h-5 w-5 opacity-0 group-hover:opacity-100 transition-opacity duration-200" 
                        fill="none" 
                        viewBox="0 0 24 24" 
                        stroke="currentColor"
                    >
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 5l7 7m0 0l-7 7m7-7H3" />
                    </svg>
                </button>
            </div>
        </div>
        </MotionCard>
        </OnlyRenderInView>
    );
}

export default SeccionOpciones;
