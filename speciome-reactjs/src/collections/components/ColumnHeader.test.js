import {fireEvent, render, screen} from "@testing-library/react";

import ColumnHeader from './ColumnHeader.js';

const columnName = 'column name';

function findIcons() {
    const ascendingIcon = screen.queryByTitle('ascending', {exact: true});
    const descendingIcon = screen.queryByTitle('descending', {exact: true});
    return {ascendingIcon, descendingIcon};
}

describe('Rendering tests without sorting', () => {
    beforeEach(() => render(<ColumnHeader name={columnName}/>));

    test('Column name is displayed', () => {
        expect(screen.queryByText(columnName, {exact: true})).not.toBeNull();
    });

    test.each(['ascending', 'descending'])('"%s" icon is displayed in grey color', (iconTitle) => {
        const icon = screen.queryByTitle(iconTitle, {exact: true});
        expect(icon).not.toBeNull();
        expect(icon.getAttribute('fill')).toBe('grey');
    });
});

describe('Rendering tests with sorting', () => {
    test('Same column - one icon displayed in black', () => {
        render(<ColumnHeader name={columnName} sortBy={columnName} sortDirection={'ascending'}/>);

        const {ascendingIcon, descendingIcon} = findIcons();

        expect(descendingIcon).toBeNull();
        expect(ascendingIcon).not.toBeNull();
        expect(ascendingIcon.getAttribute('fill')).toBe('black');
    })

    test('Other column - two columns displayed in grey', () => {
        const otherColumn = 'other column';
        render(<ColumnHeader name={columnName} sortBy={otherColumn} sortDirection={'ascending'}/>);

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

            render(<ColumnHeader
                name={columnName}
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

            render(<ColumnHeader
                name={columnName}
                sortBy={columnName}
                sortDirection={sortDirection}
                onClearSort={onClearSort}/>
            );
            return {onClearSort};
        }

        test('Ascending icon clicked - onClearSort called', () => {
            const {onClearSort} = setupMocks('ascending');
            const {ascendingIcon} = findIcons();

            fireEvent.click(ascendingIcon);
            expect(onClearSort).toHaveBeenCalledTimes(1);
        });

        test('Descending icon clicked - onSortDescending called', () => {
            const {onClearSort} = setupMocks('descending');
            const {descendingIcon} = findIcons();

            fireEvent.click(descendingIcon);
            expect(onClearSort).toHaveBeenCalledTimes(1);
        });
    });
    // Sorting selected - onClearSort
});