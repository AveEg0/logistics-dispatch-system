import type {UserFilter} from "../../api/userApi.ts";


type Props = {
    filters: UserFilter;
    onFiltersChange: (filters: UserFilter) => void;
    onReset: () => void;
};

export const UsersFilters = ({ filters, onFiltersChange, onReset }: Props) => {
    return (
        <div style={{ display: "flex", gap: "12px", marginBottom: "20px" }}>
            <input
                placeholder="Email"
                value={filters.email || ''}
                onChange={(e) =>
                    onFiltersChange({ ...filters, email: e.target.value })}
            />

            <select
                value={filters.role || ''}
                onChange={(e) =>
                    onFiltersChange({ ...filters, role: e.target.value })}
            >
                <option value="">All Roles</option>
                    <option value="ADMIN">Admin</option>
                <option value="DISPATCHER">DISPATCHER</option>
                <option value="DRIVER">DRIVER</option>
            </select>

            <button onClick={onReset}>Reset Filters</button>

        </div>
    );
}