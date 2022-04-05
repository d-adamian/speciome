import {fireEvent, render, screen} from "@testing-library/react";

import SortingIcons from './SortingIcons.js';

const columnName = 'column name';

function findIcons() {
    const ascendingIcon = screen.queryByTitle('asc', {exact: true});
    const descendingIcon = screen.queryByTitle('desc', {exact: true});
    return {ascendingIcon, descendingIcon};
}

describe('Rendering tests without sorting', () => {
    beforeEach(() => render(<SortingIcons column={columnName} displayName={columnName}/>));

    test('Column name is displayed', () => {
        expect(screen.queryByText(columnName, {exact: true})).not.toBeNull();
    });

    test.each(['asc', 'desc'])('"%s" icon is displayed in grey color', (iconTitle) => {
        const icon = screen.getByTitle(iconTitle, {exact: true});
        expect(icon).not.toBeNull();
        expect(icon.getAttribute('fill')).toBe('grey');
    });
});

describe('Rendering tests with sorting', () => {
    test('Same column - one icon displayed in black', () => {
        render(<SortingIcons column={columnName} displayName={columnName} sortBy={columnName} sortDirection={'asc'}/>);

        const {ascendingIcon, descendingIcon} = findIcons();

        expect(descendingIcon).toBeNull();
        expect(ascendingIcon).not.toBeNull();
        expect(ascendingIcon.getAttribute('fill')).toBe('black');
    })

    test('Other column - two columns displayed in grey', () => {
        const otherColumn = 'other column';
        render(<SortingIcons column={columnName} displayName={columnName} sortBy={otherColumn} sortDirection={'asc'}/>);

        const {ascendingIcon, descendingIcon} = findIcons();

        expect(descendingIcon).not.toBeNull();
        expect(descendingIcon.getAttribute('fill')).toBe('grey');
        expect(ascendingIcon).not.toBeNull();
        expect(ascendingIcon.getAttribute('fill')).toBe('grey');
    })
});

describe('Callback tests', () => {
    describe('No sorting selected', () => {
        const setupMocks = () => {
            const onSortAscending = jest.fn();
            const onSortDescending = jest.fn();

            render(<SortingIcons
                column={columnName}
                displayName={columnName}
                onSortAscending={onSortAscending}
                onSortDescending={onSortDescending}/>
            );
            return {onSortAscending, onSortDescending};
        }

        test('Ascending icon clicked - onSortAscending called', () => {
            const {onSortAscending, onSortDescending} = setupMocks();
            const {ascendingIcon} = findIcons();

            fireEvent.click(ascendingIcon);
            expect(onSortAscending).toHaveBeenCalledTimes(1);
            expect(onSortDescending).toHaveBeenCalledTimes(0);
        });

        test('Descending icon clicked - onSortDescending called', () => {
            const {onSortAscending, onSortDescending} = setupMocks();
            const {descendingIcon} = findIcons();

            fireEvent.click(descendingIcon);
            expect(onSortAscending).toHaveBeenCalledTimes(0);
            expect(onSortDescending).toHaveBeenCalledTimes(1);
        });
    });

    describe('Sorting selected', () => {
        const setupMocks = (sortDirection) => {
            const onClearSort = jest.fn();

            render(<SortingIcons
                column={columnName}
                sortBy={columnName}
                sortDirection={sortDirection}
                onClearSort={onClearSort}/>
            );
            return {onClearSort};
        }

        test('Ascending icon clicked - onClearSort called', () => {
            const {onClearSort} = setupMocks('asc');
            const {ascendingIcon} = findIcons();

            fireEvent.click(ascendingIcon);
            expect(onClearSort).toHaveBeenCalledTimes(1);
        });

        test('Descending icon clicked - onSortDescending called', () => {
            const {onClearSort} = setupMocks('desc');
            const {descendingIcon} = findIcons();

            fireEvent.click(descendingIcon);
            expect(onClearSort).toHaveBeenCalledTimes(1);
        });
    });
});