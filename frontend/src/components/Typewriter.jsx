import { useEffect, useState, cloneElement, Children } from "react";
import { motion } from "framer-motion";

const LETTER_DELAY = 0.025;
const BOX_FADE_DURATION = 0.125;
const FADE_DELAY = 5;
const MAIN_FADE_DURATION = 0.25;
const SWAP_DELAY_IN_MS = 5500;

/**
 * Componente Typewriter mejorado
 * @param {Object} props
 * @param {string[]} props.texts - Array de textos a mostrar
 * @param {React.ReactNode} props.children - Componente hijo donde se mostrará el texto
 * @param {boolean} props.fadeOut - Si es true, el texto desaparecerá después de un tiempo
 * @param {number} [props.swapDelay] - Tiempo en ms para cambiar entre textos
 * @param {number} [props.letterDelay] - Tiempo de retraso entre letras
 * @param {number} [props.fadeDelay] - Tiempo antes de que el texto desaparezca
 */
const Typewriter = ({ 
  texts = [], 
  children, 
  fadeOut = true,
  swapDelay = SWAP_DELAY_IN_MS,
  letterDelay = LETTER_DELAY,
  fadeDelay = FADE_DELAY
}) => {
  const [textIndex, setTextIndex] = useState(0);
  const [displayedTexts, setDisplayedTexts] = useState([]);
  // Nuevo estado para controlar cuándo iniciar una nueva animación
  const [animationKeys, setAnimationKeys] = useState({});

  useEffect(() => {
    if (texts.length <= 0) return;
    
    // Inicializar el primer texto
    if (!fadeOut && displayedTexts.length === 0) {
      setDisplayedTexts([texts[0]]);
      setAnimationKeys({0: Date.now()});
    }
    
    if (texts.length <= 1) return;
    
    const intervalId = setInterval(() => {
      if (fadeOut) {
        setTextIndex((prev) => (prev + 1) % texts.length);
      } else {
        setTextIndex((prev) => {
          const nextIndex = (prev + 1) % texts.length;
          
          // Agregar el nuevo texto a la lista y generar una nueva key para su animación
          if (!displayedTexts.includes(texts[nextIndex])) {
            setDisplayedTexts(prevTexts => [...prevTexts, texts[nextIndex]]);
            setAnimationKeys(prevKeys => ({
              ...prevKeys,
              [displayedTexts.length]: Date.now() // Usar la longitud actual como índice
            }));
          }
          
          return nextIndex;
        });
      }
    }, swapDelay);
    
    return () => clearInterval(intervalId);
  }, [texts, swapDelay, fadeOut, displayedTexts]);

  // Si no hay textos o no hay hijos, no renderizar nada
  if (!texts.length || !children) return null;

  // Determinar qué textos mostrar
  const textsToDisplay = fadeOut ? [texts[textIndex]] : displayedTexts;
  
  // Renderizar el componente hijo con el texto dentro
  const childWithText = Children.map(children, child => {
    // Para el caso de fadeOut=false, renderizamos cada texto acumulado
    const textContent = textsToDisplay.map((text, textIdx) => {
      // Usamos una key única para cada texto para forzar la recreación del componente
      const animKey = fadeOut ? textIndex : (animationKeys[textIdx] || Date.now());
      
      return (
        <div key={`text-${textIdx}-${animKey}`} className="mb-4">
          {text.split("").map((letter, i) => (
            <motion.span
              initial={{ opacity: fadeOut ? 1 : 1 }}
              animate={{ opacity: fadeOut ? 0 : 1 }}
              transition={{
                delay: fadeOut ? fadeDelay : 0,
                duration: MAIN_FADE_DURATION,
                ease: "easeInOut",
              }}
              key={`${textIdx}-${i}-${animKey}`}
              className="relative"
            >
              <motion.span
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{
                  delay: i * letterDelay,
                  duration: 0,
                }}
              >
                {letter}
              </motion.span>
              <motion.span
                initial={{ opacity: 0 }}
                animate={{ opacity: [0, 1, 0] }}
                transition={{
                  delay: i * letterDelay,
                  times: [0, 0.1, 1],
                  duration: BOX_FADE_DURATION,
                  ease: "easeInOut",
                }}
                className="absolute bottom-[3px] left-[1px] right-0 top-[3px] bg-gray-500"
              />
            </motion.span>
          ))}
        </div>
      );
    });

    return cloneElement(child, {}, textContent);
  });

  return <>{childWithText}</>;
};

export default Typewriter;