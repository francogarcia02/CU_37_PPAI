// components/OnlyRenderInView.jsx
import React from 'react';
import { useInView } from 'react-intersection-observer';

const OnlyRenderInView = ({ children, threshold = 0.3, triggerOnce = true, className = "", style = {} }) => {
  const { ref, inView } = useInView({
    threshold,
    triggerOnce,
  });

  return (
    <div ref={ref} className={className} style={style}>
      {inView ? children : null}
    </div>
  );
};

export default OnlyRenderInView;
