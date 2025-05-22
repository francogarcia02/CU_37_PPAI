import React, { useState, useEffect } from 'react';
import MotionCard from './MotionCard';
import OnlyRenderInView from './OnlyRenderInView';

const ORDERS_PER_PAGE = 5;

const SeccionCierreOI = ({ onBack }) => {
    const [ordenes, setOrdenes] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedOrder, setSelectedOrder] = useState(null);
    const [observacion, setObservacion] = useState('');

    useEffect(() => {
        const fetchOrdenes = async () => {
            try {
                // Usamos fetch para cargar el JSON
                const response = await fetch('/ordenesInspeccion.json');
                if (!response.ok) {
                    throw new Error('No se pudieron cargar las órdenes');
                }
                const data = await response.json();
                setOrdenes(Array.isArray(data.ordenes) ? data.ordenes : []);
            } catch (err) {
                console.error('Error:', err);
                setError('No se pudieron cargar las órdenes');
                // Datos de ejemplo en caso de error
                setOrdenes([
                    {
                        numeroDeOrden: '1',
                        fechaFinalizacion: new Date().toISOString().split('T')[0],
                        estado: 'pendiente',
                        estacionSismologica: 'Estación de Ejemplo',
                        idSismografo: '1234567890'
                    },
                    {
                        numeroDeOrden: '2',
                        fechaFinalizacion: new Date().toISOString().split('T')[0],
                        estado: 'completada',
                        estacionSismologica: 'Otra Estación',
                        idSismografo: '0987654321'
                    }
                ]);
            } finally {
                setIsLoading(false);
            }
        };

        fetchOrdenes();
    }, []);

    const handleCerrarOrden = async (e, orden) => {
        e.stopPropagation();
        try {
            // Here you would typically make an API call to your backend
            // Example: await fetch('/api/cerrar-orden', {
            //     method: 'POST',
            //     headers: { 'Content-Type': 'application/json' },
            //     body: JSON.stringify({ 
            //         ordenId: orden.numeroDeOrden,
            //         observacion 
            //     })
            // });
            console.log('Cerrando orden:', orden.numeroDeOrden, 'con observación:', observacion);
            setSelectedOrder(orden);
        } catch (err) {
            console.error('Error al cerrar la orden:', err);
            setError('Error al procesar el cierre de la orden');
        }
    };

    const handleSubmitObservacion = async () => {
        try {
            // Here you would typically make an API call to your backend
            // to save the observation and close the order
            console.log('Guardando observación para orden:', selectedOrder.numeroDeOrden, 'Observación:', observacion);
            
            // After successful submission, you might want to:
            // 1. Show a success message
            // 2. Reset the form
            // 3. Go back to the orders list
            setSelectedOrder(null);
            setObservacion('');
            // Optionally refresh the orders list
            // fetchOrdenes();
        } catch (err) {
            console.error('Error al guardar la observación:', err);
            setError('Error al guardar la observación');
        }
    };

    // Calcular el total de páginas
    const totalPages = Math.ceil(ordenes.length / ORDERS_PER_PAGE);
    
    // Obtener órdenes para la página actual
    const currentOrders = ordenes.slice(
        (currentPage - 1) * ORDERS_PER_PAGE,
        currentPage * ORDERS_PER_PAGE
    );

    const handlePageChange = (newPage) => {
        setCurrentPage(prev => Math.max(1, Math.min(totalPages, newPage)));
    };

    if (isLoading) {
        return <div className="p-4">Cargando órdenes...</div>;
    }

    if (error) {
        return <div className="p-4 text-red-500">{error}</div>;
    }

    return (
        <OnlyRenderInView>
            <MotionCard>
                {!selectedOrder ? (
                    // Order List View
                    <div className="w-auto h-auto flex flex-col bg-cyan-950 rounded-xl text-white p-2 sm:p-4 md:p-6">
                        <div className="mb-4 sm:mb-6">
                            <div className="flex items-center mb-2">
                                <button 
                                    onClick={onBack}
                                    className="mr-3 p-1 rounded-full hover:bg-cyan-800 transition-colors duration-200"
                                    aria-label="Volver"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                                    </svg>
                                </button>
                                <h2 className="text-xl sm:text-2xl font-bold">Cerrar Orden de Inspección</h2>
                            </div>
                            <p className="text-cyan-200 text-sm sm:text-base ml-9">Seleccione una orden para continuar</p>
                        </div>
    
                        <div className="bg-cyan-900 rounded-xl p-2 sm:p-4 mb-4 w-full overflow-auto flex-1">
                            {/* Tabla para pantallas medianas y grandes */}
                            <div className="hidden md:block">
                                <div className="grid grid-cols-12 gap-2 font-semibold mb-3 px-2 text-sm">
                                    <div className="col-span-2">N° Orden</div>
                                    <div className="col-span-2">Fecha</div>
                                    <div className="col-span-2">Estado</div>
                                    <div className="col-span-3">Estación</div>
                                    <div className="col-span-2">ID Sismógrafo</div>
                                    <div className="col-span-1">Acción</div>
                                </div>
                                
                                <div className="space-y-2">
                                    {currentOrders.map((orden) => (
                                        <div 
                                            key={orden.numeroDeOrden} 
                                            className="grid grid-cols-12 gap-2 items-center bg-cyan-800 hover:bg-cyan-700 transition-colors duration-200 rounded-lg p-2 text-sm"
                                        >
                                            <div className="col-span-2 font-medium truncate" title={orden.numeroDeOrden}>#{orden.numeroDeOrden}</div>
                                            <div className="col-span-2">{orden.fechaFinalizacion || 'N/A'}</div>
                                            <div className="col-span-2">
                                                <span className="px-2 py-1 rounded-full text-xs flex items-center justify-center w-full truncate">
                                                    {orden.estado}
                                                </span>
                                            </div>
                                            <div className="col-span-3 truncate" title={orden.estacionSismologica}>{orden.estacionSismologica}</div>
                                            <div className="col-span-2 truncate" title={orden.idSismografo}>{orden.idSismografo}</div>
                                            <div className="col-span-1 flex justify-end">
                                                <button 
                                                    className="bg-blue-500 hover:bg-blue-600 transition-colors duration-200 text-white px-2 sm:px-3 py-1 rounded text-xs sm:text-sm cursor-pointer whitespace-nowrap"
                                                    onClick={(e) => handleCerrarOrden(e, orden)}
                                                >
                                                    Cerrar
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
    
                            {/* Vista de tarjeta para móviles */}
                            <div className="md:hidden space-y-3">
                                {currentOrders.map((orden) => (
                                    <div 
                                        key={orden.numeroDeOrden} 
                                        className="bg-cyan-800 rounded-lg p-3 space-y-2"
                                    >
                                        <div className="flex justify-between items-start">
                                            <div>
                                                <div className="font-medium">#{orden.numeroDeOrden}</div>
                                                <div className="text-cyan-300 text-sm">{orden.estacionSismologica}</div>
                                            </div>
                                            <span className={`px-2 py-1 rounded-full text-xs ${
                                                orden.estado.toLowerCase().includes('pendiente') ? 'bg-yellow-500 text-yellow-900' :
                                                orden.estado.toLowerCase().includes('complet') ? 'bg-green-500 text-green-900' :
                                                'bg-gray-500 text-gray-900'
                                            }`}>
                                                {orden.estado}
                                            </span>
                                        </div>
                                        <div className="grid grid-cols-2 gap-2 text-sm">
                                            <div>
                                                <div className="text-cyan-400 text-xs">Fecha</div>
                                                <div>{orden.fechaFinalizacion || 'N/A'}</div>
                                            </div>
                                            <div>
                                                <div className="text-cyan-400 text-xs">Sismógrafo</div>
                                                <div>{orden.idSismografo}</div>
                                            </div>
                                        </div>
                                        <button 
                                            className="w-full mt-2 bg-blue-500 hover:bg-blue-600 transition-colors duration-200 text-white py-1 rounded text-sm"
                                            onClick={(e) => handleCerrarOrden(e, orden)}
                                        >
                                            Cerrar Orden
                                        </button>
                                    </div>
                                ))}
                            </div>
    
                            {/* Controles de paginación */}
                            {totalPages > 1 && (
                                <div className="flex flex-col sm:flex-row justify-between items-center mt-4 pt-4 border-t border-cyan-700 space-y-2 sm:space-y-0">
                                    <button 
                                        onClick={() => handlePageChange(currentPage - 1)}
                                        disabled={currentPage === 1}
                                        className="w-full sm:w-auto px-4 py-2 bg-cyan-800 rounded disabled:opacity-50 text-sm"
                                    >
                                        Anterior
                                    </button>
                                    
                                    <div className="text-sm">
                                        Página {currentPage} de {totalPages}
                                    </div>
                                    
                                    <button 
                                        onClick={() => handlePageChange(currentPage + 1)}
                                        disabled={currentPage === totalPages}
                                        className="w-full sm:w-auto px-4 py-2 bg-cyan-800 rounded disabled:opacity-50 text-sm"
                                    >
                                        Siguiente
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>
                ) : (
                    // Order Detail View
                    <div className="w-auto h-auto flex flex-col bg-cyan-950 rounded-xl text-white p-6">
                        <div className="mb-6">
                            <button 
                                onClick={() => setSelectedOrder(null)}
                                className="flex items-center text-cyan-300 hover:text-white mb-4"
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-1" viewBox="0 0 20 20" fill="currentColor">
                                    <path fillRule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clipRule="evenodd" />
                                </svg>
                                Volver al listado
                            </button>
                            <h2 className="text-2xl font-bold mb-2">Cerrar Orden #{selectedOrder.numeroDeOrden}</h2>
                            
                            <div className="bg-cyan-900 rounded-lg p-4 mb-6">
                                <h3 className="text-lg font-semibold mb-3">Detalles de la Orden</h3>
                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div>
                                        <p className="text-cyan-300 text-sm">Estación Sismológica</p>
                                        <p className="text-white">{selectedOrder.estacionSismologica}</p>
                                    </div>
                                    <div>
                                        <p className="text-cyan-300 text-sm">ID Sismógrafo</p>
                                        <p className="text-white">{selectedOrder.idSismografo}</p>
                                    </div>
                                    <div>
                                        <p className="text-cyan-300 text-sm">Fecha de Finalización</p>
                                        <p className="text-white">{selectedOrder.fechaFinalizacion || 'N/A'}</p>
                                    </div>
                                    <div>
                                        <p className="text-cyan-300 text-sm">Estado</p>
                                        <p className="text-white">{selectedOrder.estado}</p>
                                    </div>
                                </div>
                            </div>
    
                            <div className="mb-6">
                                <label htmlFor="observacion" className="block text-sm font-medium text-cyan-200 mb-2">
                                    Observación
                                </label>
                                <textarea
                                    id="observacion"
                                    rows="4"
                                    className="w-full px-3 py-2 bg-cyan-900 border border-cyan-700 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-white"
                                    placeholder="Ingrese una observación sobre el cierre de la orden..."
                                    value={observacion}
                                    onChange={(e) => setObservacion(e.target.value)}
                                    required
                                ></textarea>
                            </div>
    
                            <div className="flex justify-end space-x-3">
                                <button
                                    onClick={() => {
                                        setSelectedOrder(null);
                                        setObservacion('');
                                    }}
                                    className="px-4 py-2 bg-gray-600 hover:bg-gray-700 rounded-lg text-white"
                                >
                                    Cancelar
                                </button>
                                <button
                                    onClick={handleSubmitObservacion}
                                    disabled={!observacion.trim()}
                                    className={`px-4 py-2 rounded-lg text-white ${
                                        observacion.trim() 
                                            ? 'bg-green-600 hover:bg-green-700' 
                                            : 'bg-gray-500 cursor-not-allowed'
                                    }`}
                                >
                                    Confirmar Cierre
                                </button>
                            </div>
                        </div>
                    </div>
                )}
            </MotionCard>
        </OnlyRenderInView>
    );
}   

export default SeccionCierreOI;
