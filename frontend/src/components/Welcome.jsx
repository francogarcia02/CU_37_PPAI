import UTNLogo from "./UTNLogo";
import Typewriter from "./Typewriter";


const Welcome = () => {
    return (
        <section className="w-screen h-screen flex items-center justify-center p-1 md:p-2">
        <div className=" h-[50%] w-[50%] flex flex-row items-center justify-center bg-[#f4f5f7] relative">
            <UTNLogo />
            <div className="flex flex-col items-center">
            <Typewriter texts={["UNIVERSIDAD TECNOLÓGICA NACIONAL"]} fadeOut={false} letterDelay={0.035}>
                <h2 className="text-black text-[2vw]  font-bold  leading-none whitespace-nowrap">
                    {/* acá se insertará el texto  */}
                </h2>
            </Typewriter>
            <Typewriter texts={["Catedra: Diseño de sistemas de información"]} fadeOut={false} letterDelay={0.035}>
                <h2 className="text-black text-[2vw]  font-bold  leading-none whitespace-nowrap">
                    {/* acá se insertará el texto  */}
                </h2>
            </Typewriter>
            <Typewriter texts={["PPAI grupo 13 - CU: Cerrar Orden de inspeccion a ES"]} fadeOut={false} letterDelay={0.035}>
                <p className="text-black text-[1vw]  font-bold  leading-none whitespace-nowrap">
                    {/* acá se insertará el texto  */}
                </p>
            </Typewriter>
            </div>
        </div>
        </section>
    );
}

export default Welcome;

