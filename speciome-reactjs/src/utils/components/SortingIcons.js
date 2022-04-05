import {SortDown, SortUp} from 'react-bootstrap-icons';

const ascendingTitle = 'asc';
const descendingTitle = 'desc';

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

function SortingIcons(props) {
    const {column, displayName, sortBy, sortDirection, onSortAscending, onSortDescending, onClearSort} = props;

    const sameColumn = column === sortBy;
    const showAscending = sameColumn ? sortDirection === ascendingTitle : true;
    const ascendingSelected = sameColumn && sortDirection === ascendingTitle;

    const showDescending = sameColumn ? sortDirection === descendingTitle : true;
    const descendingSelected = sameColumn && sortDirection === descendingTitle;
    return (
        <div>
            {displayName}
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

export default SortingIcons;