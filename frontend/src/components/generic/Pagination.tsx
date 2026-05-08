type Props = {
    page: number;
    totalPages: number;

    onPageChange: (
        page: number
    ) => void;
};

export const Pagination = ({
                               page,
                               totalPages,
                               onPageChange,
                           }: Props) => {

    const isFirstPage = page === 0;

    const isLastPage =
        totalPages === 0 ||
        page + 1 >= totalPages;

    return (
        <div
            style={{
                marginTop: 20,
                display: "flex",
                alignItems: "center",
                gap: 12,
            }}
        >

            <button
                disabled={isFirstPage}

                onClick={() =>
                    onPageChange(
                        Math.max(page - 1, 0)
                    )
                }
            >
                Previous
            </button>

            <span>
        Page {page + 1} / {totalPages}
      </span>

            <button
                disabled={isLastPage}

                onClick={() => {

                    if (!isLastPage) {
                        onPageChange(page + 1);
                    }
                }}
            >
                Next
            </button>

        </div>
    );
};