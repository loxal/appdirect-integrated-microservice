# Backlog

## next
    retrieve /token from YaaS only when necessary
    as part of an integration test, populate sample questions | link iOS user app
	Creating a Ballot/Vote validator essentially introduces questionnaire business logic
		Interviewer app
    make it possible to run integration tests in parallel in the IDE and on the CI on the same machine
        both apps should not run on the same port (one could be random)
## later
    replace “response.readEntity(javaClass<String>())” with
        asyncResponse.resume(Response.fromResponse(response).build()) for GET resp.
        asyncResponse.resume(Response.fromResponse(response).type(MediaType.APPLICATION_JSON_TYPE).build()) for DELETE
    usage ImageJ? to create a series of iOS App Store images http://rsbweb.nih.gov/ij/
	Mirror return via GET what was POSTED or provided via GET
## dream
    Voting/Polling/Ballot service
        Planning poker service
    Marathon ranking service
    Something to check the "Reisepass readiness" status
    getVoucher (UUID), getVisitCount


