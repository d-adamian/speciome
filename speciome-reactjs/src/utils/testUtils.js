import {rest} from 'msw';
import {setupServer} from 'msw/node';


function setupEmptyResponseServer(endpointUrl, statusCode) {
    const server = setupServer(
        rest.post(endpointUrl, (req, res, ctx) => {
            return res(ctx.status(statusCode), ctx.json({}));
        }),
    )

    beforeAll(() => server.listen());
    afterEach(() => server.resetHandlers());
    afterAll(() => server.close());

    return server;
}

function changeEmptyResponseCode(server, endpointUrl, statusCode) {
    server.use(
        rest.post(endpointUrl, (req, res, ctx) => {
            return res(ctx.status(statusCode), ctx.json({}));
        }),
    );
}

export {changeEmptyResponseCode, setupEmptyResponseServer};