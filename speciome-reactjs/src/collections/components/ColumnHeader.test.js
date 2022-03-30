import {render, screen} from "@testing-library/react";

import ColumnHeader from './ColumnHeader.js';

const columnName = 'column name';

describe('Rendering tests without sorting', () => {
    beforeEach(() => render(<ColumnHeader name={columnName}/>));

    test('Column name is displayed', () => {
        expect(screen.queryByText(columnName)).not.toBeNull();
    });

    test.each(['ascending', 'descending'])('"%s" icon is displayed in grey color', (iconTitle) => {
        const icon = screen.queryByTitle(iconTitle);
        expect(icon).not.toBeNull();
        expect(icon.getAttribute('fill')).toBe('grey');
    });
});

describe('Rendering tests with sorting', () => {
    test('Same column - one icon displayed in black', () => {
        render(<ColumnHeader name={columnName} sortBy={columnName} sortDirection={'ascending'}/>);

        const ascendingIcon = screen.queryByTitle('ascending');
        const descendingIcon = screen.queryByTitle('descending');

        expect(descendingIcon).toBeNull();
        expect(ascendingIcon).not.toBeNull();
        expect(ascendingIcon.getAttribute('fill')).toBe('black');
    })

    test('Other column - two columns displayed in grey', () => {
        const otherColumn = 'other column';
        render(<ColumnHeader name={columnName} sortBy={otherColumn} sortDirection={'ascending'}/>);

        const ascendingIcon = screen.queryByTitle('ascending');
        const descendingIcon = screen.queryByTitle('descending');

        expect(descendingIcon).not.toBeNull();
        expect(descendingIcon.getAttribute('fill')).toBe('grey');
        expect(ascendingIcon).not.toBeNull();
        expect(ascendingIcon.getAttribute('fill')).toBe('grey');
    })
});