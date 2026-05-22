import {startTransition, useCallback, useEffect, useState} from "react";
import {fetchOrders, type OrderFilter} from "../api/ordersApi";
import type { Order } from "../api/ordersApi";
import { OrdersTable } from "../components/orders/OrdersTable";
import { OrdersFilters } from "../components/orders/OrdersFilters";
import {Pagination} from "../components/generic/Pagination.tsx";
import {type Driver, fetchDrivers} from "../api/driverApi.ts";

const Orders = () => {
    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(0);
    const [size] = useState(10);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [debounceTimeout] = useState<number>(500);
    const [drivers, setDrivers] = useState<Driver[]>([]);
    const DRIVER_STATUS_AVAILABLE = "AVAILABLE";
    const DRIVER_SELECT_LIMIT = 100;

    const defaultFilters: OrderFilter = {
        search: "",
        status: "",
    };
    const [filters, setFilters] = useState<OrderFilter>(defaultFilters);

    const [debouncedSearch, setDebouncedSearch] = useState(filters.search);

    const [sort, setSort] = useState<{
        field: string;
        direction: "asc" | "desc";
    }>({
        field: "createdAt",
        direction: "desc",
    });

    const handleSort = (field: string) => {
        setSort((prevSort) => ({
            ...prevSort,
            field,
            direction: prevSort.field === field ? (prevSort.direction === "asc" ? "desc" : "asc") : "desc",
        }));
    }

    const handleResetFilters = () => {
        setFilters(defaultFilters);
        setPage(0);
    }
    
    const loadOrders = useCallback(async () => {

        try {

            const data = await fetchOrders(
                page,
                size,
                sort,
                {
                    ...filters,
                    search: debouncedSearch,
                }
            );

            startTransition(() => {
                setOrders(data.content);
                setTotalPages(data.page.totalPages);
            });

        } catch (e) {

            console.error(
                "Failed to load orders:",
                e
            );
        }
    }, [page, size, sort, filters, debouncedSearch]);

    const loadDrivers = async () => {
        try {
            const data = await fetchDrivers(0, DRIVER_SELECT_LIMIT,
                {field: "name", direction: "asc"}, {status: DRIVER_STATUS_AVAILABLE});
            startTransition(() => {
                setDrivers(data.content);
            })
        } catch (e) {
            console.error("Failed to load drivers:", e);
        }

    };

    const handleAssign = async () => {
        await loadOrders();
        await loadDrivers();
    }

    useEffect(() => {

        void loadOrders();
        void loadDrivers();

    }, [loadOrders]);



    useEffect(() => {

        const handler = setTimeout(() => {
            setDebouncedSearch(filters.search);
        }, debounceTimeout);

        return () => clearTimeout(handler);
    }, [filters.search, debounceTimeout]);
    

    return (
        <div>
            <h1>Orders</h1>

            <OrdersFilters
                filters={filters}
                onFiltersChange={(newFilters) => {
                    setFilters(newFilters);
                    setPage(0);
                }}
                onReset={handleResetFilters}
            />

            <OrdersTable
                orders={orders}
                onSort={handleSort}
                sort={sort}
                drivers={drivers}
                onAssigned={handleAssign}
            />

            <Pagination
                page={page}
                totalPages={totalPages}
                onPageChange={setPage}
            />

        </div>
    );
};
export default Orders