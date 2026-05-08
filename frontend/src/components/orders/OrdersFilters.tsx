import type {OrderFilter} from "../../api/ordersApi.ts";

type Props = {
    filters: OrderFilter;
    onFiltersChange: (filters: OrderFilter) => void;
    onReset: () => void;
};

export const OrdersFilters = ({
                                  filters,
                                  onFiltersChange,
                                  onReset,
                              }: Props) => {

    const toInputDateTime = (value?: string) => {
        if (!value) return '';
        return  value.slice(0, 16);
    }

    return (
        <div style={{
            display: "flex",
            gap: "12px",
            marginBottom: "20px",
            flexWrap: "wrap"}}>
            <input
                placeholder="Search"
                value={filters.search || ''}
                onChange={(e) =>
                    onFiltersChange({...filters, search: e.target.value})}/>

            <select
            value={filters.status || ''}
            onChange={(e) =>
                onFiltersChange({...filters, status: e.target.value})}>
                <option value="">All</option>
                <option value="CREATED">CREATED</option>
                <option value="ASSIGNED">ASSIGNED</option>
                <option value="IN_PROGRESS">IN_PROGRESS</option>
                <option value="COMPLETED">COMPLETED</option>
                <option value="CANCELLED">CANCELLED</option>
                </select>

            <input type="datetime-local"
            value={toInputDateTime(filters.from)}
            onChange={(e) =>
                onFiltersChange({
                    ...filters,
                    from: e.target.value ? new Date(e.target.value).toISOString() : ''})}/>


            <input type="datetime-local"
            value={toInputDateTime(filters.to)}
            onChange={(e) =>
                onFiltersChange({
                    ...filters,
                    to: e.target.value ? new Date(e.target.value).toISOString() : ''})}/>

            <button onClick={onReset}>
                Reset Filters
            </button>

        </div>
    );
};