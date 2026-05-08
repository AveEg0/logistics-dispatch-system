import {useEffect, useState} from "react";
import {type Driver, type DriverFilter, fetchDrivers} from "../api/driverApi.ts";
import {Table} from "../components/generic/Table.tsx";
import {DriverStatusBadge} from "../components/drivers/ui/DriverStatusBadge.tsx";
import {Pagination} from "../components/generic/Pagination.tsx";


export const Drivers = () => {
    const [drivers, setDrivers] = useState<Driver[]>([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [size] = useState(10);
    const [sort, setSort] = useState({ field: "name", direction: "asc" as "asc" | "desc"});
    const [filters, setFilters] = useState<DriverFilter>({search: "", status: ""});
    const [debouncedSearch, setDebouncedSearch] = useState(filters.search || "");
    const [debounceTimeout] = useState<number>(500);
    const handleSort = (field: string) => {
        setSort((prevSort) => ({
            field,
            direction:
                prevSort.field === field && prevSort.direction === 'asc' ? 'desc' : 'asc',
        }));
    }


    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedSearch(filters.search || "");
        }, debounceTimeout);

        return () => clearTimeout(handler);

        }, [debounceTimeout, filters.search]);


    useEffect(() => {


        const loadDrivers = async () => {
            try {
                const data = await fetchDrivers(page, size, sort, {...filters, search: debouncedSearch});
                console.log(data);
                setDrivers(data.content);
                setTotalPages(Number(data.page.totalPages));
            } catch (e) {
                console.error("Failed to load drivers:", e);
            }
        };
        loadDrivers();
    }, [page, size, sort, filters, totalPages, debouncedSearch]);

    return (
        <div>
            <h1>Drivers</h1>

            <div style={{display: "flex", gap: "12px", marginBottom: "20px"}}>
                <input
                    placeholder="Search"
                    value={filters.search}
                    onChange={(e) =>
                        setFilters((prev) =>
                            ({...prev, search: e.target.value}))}/>

                <select
                    value={filters.status}
                    onChange={(e) =>
                        setFilters((prev) =>
                            ({...prev, status: e.target.value}))}>
                    <option value="">All</option>
                    <option value="AVAILABLE">AVAILABLE</option>
                    <option value="BUSY">BUSY</option>
                    <option value="OFFLINE">OFFLINE</option>
                    <option value="RESERVED">RESERVED</option>
                </select>
            </div>


            <Table<Driver>
                data={drivers}
                columns={[
                    {key: "id", label: "ID", sortable: true},
                    {key: "name", label: "Name", sortable: true},
                    {key: "status", label: "Status",
                        render: (driver) => (<DriverStatusBadge status={driver.status} />)},
                    {key: "currentLocation", label: "Location",
                        render: (driver) =>
                            driver.currentLocation ? driver.currentLocation : 'Location not available'},
                    {key: "email", label: "Email"},
                ]}
                sort={sort}
                onSort={handleSort}
            />


            <Pagination
                page={page}
                totalPages={totalPages}
                onPageChange={setPage}
            />

        </div>
    )

}