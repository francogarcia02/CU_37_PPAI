import React from 'react';
import { useForm } from 'react-hook-form';
import MotionCard from './MotionCard';
import OnlyRenderInView from './OnlyRenderInView';
  
const LogIn = ({ setSeHaLogueado, setSesion }) => {

    const {
        register,
        handleSubmit,
        formState: { errors },
      } = useForm();
    
      const onSubmit = (data) => {
        console.log('Datos enviados:', data);
        setSeHaLogueado(true);
        setSesion({
          fechaHoraInicio: new Date(),
          nombreUsuario: data.username,
          contraseña: data.password,
          fechaHoraFin: null
        });
      };

    return (
        <OnlyRenderInView>
          <MotionCard>
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-2xl shadow-xl w-full max-w-sm">
        <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Iniciar Sesión</h2>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Usuario</label>
            <input
              type="text"
              {...register('username', { required: 'El nombre de usuario es obligatorio' })}
              className="mt-1 w-full px-4 py-2 border rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="Usuario"
            />
            {errors.username && <p className="text-red-500 text-sm">{errors.username.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Contraseña</label>
            <input
              type="password"
              {...register('password', { required: 'La contraseña es obligatoria' })}
              className="mt-1 w-full px-4 py-2 border rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-400"
              placeholder="••••••••"
            />
            {errors.password && <p className="text-red-500 text-sm">{errors.password.message}</p>}
          </div>

          <button
            type="submit"
            className="w-full bg-blue-500 text-white py-2 rounded-xl hover:bg-blue-600 transition duration-200"
          >
            Iniciar sesión
          </button>
        </form>
      </div>
    </div>
    </MotionCard>
    </OnlyRenderInView>
  );

}

export default LogIn;
