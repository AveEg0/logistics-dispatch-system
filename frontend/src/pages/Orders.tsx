import { useEffect, useState } from "react";
import {fetchOrders, type OrderFilter} from "../api/ordersApi";
import type { Order } from "../api/ordersApi";
import { OrdersTable } from "../components/orders/OrdersTable";
import { OrdersFilters } from "../components/orders/OrdersFilters";
import {Pagination} from "../components/generic/Pagination.tsx";

export const Orders = () => {
    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(0);
    const [status] = useState<string>("");
    const [size] = useState(10);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [debounceTimeout] = useState<number>(500);

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

    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedSearch(filters.search);
        }, debounceTimeout);

        return () => clearTimeout(handler);
    }, [filters.search, debounceTimeout]);

    useEffect(() => {
        const load = async () => {
            try {
                const data = await fetchOrders(page, size, sort,{
                    ...filters,
                    search: debouncedSearch,
                });

                setOrders(data.content);
                setTotalPages(data.page.totalPages);
                console.log(data.totalPages, " TOTAL PAGES ", totalPages);
            } catch (e) {
                console.error("Failed to load orders:", e);
            }
        };

        load();
    }, [page, size, sort, status, filters, debouncedSearch, totalPages]);

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
            />

            <Pagination
                page={page}
                totalPages={totalPages}
                onPageChange={setPage}
            />

        </div>
    );
};