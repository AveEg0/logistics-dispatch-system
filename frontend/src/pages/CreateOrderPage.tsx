import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createOrder } from '../api/ordersApi';

export const CreateOrderPage = () => {

    const navigate = useNavigate();

    const [pickupLocation, setPickupLocation] = useState('');
    const [deliveryLocation, setDeliveryLocation] = useState('');
    const [description, setDescription] = useState('');
    const [loadings, setLoadings] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.SubmitEvent) => {

        e.preventDefault();

        try {
            setLoadings(true);
            await createOrder({
                pickupLocation,
                deliveryLocation,
                description
            });
            navigate('/orders');
        } catch (err) {
            console.error('Failed to create order:', err);
            setError('Failed to create order');
        } finally {
            setLoadings(false);
        }
    };

    return (
        <div style={{ margin: '0 auto', maxWidth: '700px', padding: '24px' }}>
            <h1 style={{marginBottom: '24px'}}>Create Order</h1>
            <form
                onSubmit={handleSubmit}
                style={{ flexDirection: 'column', gap: '16px' }}>

                <div>
                    <label>Pickup Location:</label>
                    <input
                        type="text"
                        value={pickupLocation}
                        onChange={(e) => setPickupLocation(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Delivery Location:</label>
                    <input
                        type="text"
                        value={deliveryLocation}
                        onChange={(e) => setDeliveryLocation(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Description:</label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                { error && <div style={{ color: 'red' }}>{error}</div> }

                <button type="submit" disabled={loadings}>
                    {loadings ? 'Creating...' : 'Create Order'}
                </button>
            </form>
        </div>
    )


};