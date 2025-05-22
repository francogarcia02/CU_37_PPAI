import { motion } from "framer-motion";

export default function UTNLogo() {
  return (
    
    <motion.svg
      width="300"
      height="300"
      viewBox="0 0 220 350"
      xmlns="http://www.w3.org/2000/svg"
      className="w-full h-auto"
      preserveAspectRatio="xMidYMid meet"
      initial="hidden"
      animate="visible"
    >
      <g transform="translate(40, 20)">
      {/* Animaciones tipo trazo */}
      <motion.path
        d="M30,0 A70,70 0 0 0 170,0"
        stroke="black"
        strokeWidth="40"
        fill="none"
        transform="translate(10,20)"
        variants={{
          hidden: { pathLength: 0 },
          visible: { pathLength: 1, transition: { duration: 1 } },
        }}
      />

      <motion.path
        d="M30,0 A70,70 0 0 1 170,0"
        stroke="black"
        strokeWidth="40"
        fill="none"
        transform="translate(10,260)"
        variants={{
          hidden: { pathLength: 0 },
          visible: { pathLength: 1, transition: { duration: 1, delay: 0.5 } },
        }}
      />

      {/* Cruz vertical */}
      <motion.rect
        x="90"
        y="20"
        width="40"
        height="240"
        fill="black"
        transform="translate(0,-20)"
        initial={{ scaleY: 0 }}
        animate={{ scaleY: 1 }}
        transition={{ duration: 0.5, delay: 1 }}
        style={{ transformOrigin: "center top" }}
      />

      {/* Cruz horizontal */}
      <motion.rect
        x="20"
        y="120"
        width="180"
        height="40"
        fill="black"
        transform="translate(0,10)"
        initial={{ scaleX: 0 }}
        animate={{ scaleX: 1 }}
        transition={{ duration: 0.5, delay: 1.2 }}
        style={{ transformOrigin: "left center" }}
      />

      {/* Texto UTN */}
      <motion.text
        x="108"
        y="320"
        fontFamily="Arial, sans-serif"
        fontSize="50"
        fill="black"
        textAnchor="middle"
        fontWeight="bold"
        transform="translate(8,30)"
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 1.8 }}
      >
        U T N
      </motion.text>
      </g>
    </motion.svg>
      
  );
}
