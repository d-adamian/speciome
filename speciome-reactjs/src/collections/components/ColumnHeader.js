import {SortDown, SortUp} from 'react-bootstrap-icons';

const ascendingTitle = 'ascending';
const descendingTitle = 'descending';

const selectedColor = 'black';
const notSelectedColor = 'grey'

function AscendingIconNotSelected(props) {
    const {onSortAscending} = props;
    return <SortUp title={ascendingTitle} color={notSelectedColor} onClick={onSortAscending}/>;
}

function AscendingIconSelected(props) {
    const {onClearSort} = props;
    return <SortUp title={ascendingTitle} color={selectedColor} onClick={onClearSort}/>;
}

function AscendingIcon(props) {
    const {selected, onSortAscending, onClearSort} = props;
    return selected ?
        <AscendingIconSelected onClearSort={onClearSort}/> :
        <AscendingIconNotSelected onSortAscending={onSortAscending}/>;
}

function DescendingIconNotSelected(props) {
    const {onSortDescending} = props;
    return <SortDown title={descendingTitle} color={notSelectedColor} onClick={onSortDescending}/>;
}

function DescendingIconSelected(props) {
    const {onClearSort} = props;
    return <SortDown title={descendingTitle} color={selectedColor} onClick={onClearSort}/>;
}

function DescendingIcon(props) {
    const {selected, onSortDescending, onClearSort} = props;
    return selected ?
        <DescendingIconSelected onClearSort={onClearSort}/> :
        <DescendingIconNotSelected onSortDescending={onSortDescending}/>;
}

function ColumnHeader(props) {
    const {name, sortBy, sortDirection, onSortAscending, onSortDescending, onClearSort} = props;

    const sameColumn = name === sortBy;
    const showAscending = sameColumn ? sortDirection === 'ascending' : true;
    const ascendingSelected = sameColumn && sortDirection === 'ascending';

    const showDescending = sameColumn ? sortDirection === 'descending' : true;
    const descendingSelected = sameColumn && sortDirection === 'descending';
    return (
        <div>
            {name}
            {showAscending ?
                <AscendingIcon
                    selected={ascendingSelected}
                    onSortAscending={onSortAscending}
                    onClearSort={onClearSort}
                />
                : <div/>
            }
            {showDescending ?
                <DescendingIcon
                    selected={descendingSelected}
                    onSortDescending={onSortDescending}
                    onClearSort={onClearSort}
                />
                : <div/>
            }
        </div>
    );
}

export default ColumnHeader;