import { SortDown, SortUp } from 'react-bootstrap-icons';

function ColumnHeader(props) {
    const {name, sortBy, sortDirection} = props;

    const sameColumn = name === sortBy;
    const showAscending = sameColumn ? sortDirection === 'ascending' : true;
    const ascendingColor = (sameColumn && sortDirection === 'ascending') ? 'black' : 'grey';

    const showDescending = sameColumn ? sortDirection === 'descending' : true;
    const descendingColor = (sameColumn && sortDirection === 'descending') ? 'black' : 'grey';
    return (
        <div>
            {name}
            {showAscending ? <SortUp title='ascending' color={ascendingColor}/> : <div/>}
            {showDescending ? <SortUp title='descending' color={descendingColor}/> : <div/>}
        </div>
    );
}

export default ColumnHeader;