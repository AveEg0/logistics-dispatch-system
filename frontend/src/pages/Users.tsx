import {useEffect, useState} from "react";
import {fetchUsers, type User, type UserFilter} from "../api/userApi.ts";
import {UsersFilters} from "../components/users/UsersFilters.tsx";
import {Table} from "../components/generic/Table.tsx";
import {EnabledBadge} from "../components/users/ui/EnabledBadge.tsx";
import {Pagination} from "../components/generic/Pagination.tsx";

export const Users = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [role] = useState("");
    const [debounceTimeout] = useState<number>(500);
    const [size] = useState(10);
    const defaultFilters: UserFilter = {
        email: '',
        role: '',
    };
    const [filters, setFilters] = useState<UserFilter>(defaultFilters);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [sort, setSort] = useState({ field: "createdAt",
        direction: "desc" as "asc" | "desc"});
    const [debouncedEmail, setDebouncedEmail] = useState(filters.email);

    const handleResetFilters = () => {
        setFilters(defaultFilters);
        setPage(0);
    }
    const handleSort = (field: string) => {
        setSort((prevState) =>
            ({ field,
                direction:
                    prevState.field === field && prevState.direction === 'asc' ? 'desc' : 'asc' }));
    }
    
    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedEmail(filters.email);
        }, debounceTimeout);
    
        return () => clearTimeout(handler);
    }, [debounceTimeout, filters.email]);
    
    
    
    useEffect(() => {
        const loadUsers = async () => {
            const data = await fetchUsers(page, size, sort, {
                email: debouncedEmail,
                role: role,
            });

            setUsers(data.content);
            setTotalPages(data.page.totalPages);
        };
        loadUsers();
    }, [page, size, sort, filters, totalPages, debouncedEmail, role]);


    return (
        <div>
            <h1>Users</h1>

            <UsersFilters
                filters={filters}
                onFiltersChange={(newFilters) => {
                    setFilters(newFilters);
                    setPage(0);
                }}
                onReset={handleResetFilters}
            />

            <Table<User>
                data={users}
                sort={sort}
                onSort={handleSort}
                columns={[
                    { key: "id", label: "ID" , sortable: true},
                    { key: "email", label: "Email", sortable: true },
                    { key: "role", label: "Role", sortable: true },
                    { key: "enabled", label: "Enabled",
                        render: (user) => <EnabledBadge enabled={user.enabled} />
                    },
                    { key: "createdAt", label: "Created", sortable: true,
                    render: (user) => new Date(user.createdAt).toLocaleString()},
                ]}
            />

            <Pagination
                page={page}
                totalPages={totalPages}
                onPageChange={setPage}
            />

        </div>
    )

}


