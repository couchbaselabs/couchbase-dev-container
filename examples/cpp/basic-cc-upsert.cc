/*
File: basic-cc-upsert.cc 
Description: Upsert

This example shows an upsert of a document and then a retrieval of a portion of that document via the subdocument API.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/c-sdk/current/howtos/kv-operations.html">Key Value Operations in C/C++</a>.
*/

#include <vector>
#include <string>
#include <iostream>

#include <libcouchbase/couchbase.h>

static void
check(lcb_STATUS err, const char* msg)
{
    if (err != LCB_SUCCESS) {
        std::cerr << "[ERROR] " << msg << ": " << lcb_strerror_short(err) << "\n";
        exit(EXIT_FAILURE);
    }
}

struct Result {
    std::string value{};
    lcb_STATUS status{ LCB_SUCCESS };
};

struct SubdocResults {
    lcb_STATUS status{ LCB_SUCCESS };
    std::vector<Result> entries{};
};

static void
sdget_callback(lcb_INSTANCE*, int, const lcb_RESPSUBDOC* resp)
{
    SubdocResults* results = nullptr;
    lcb_respsubdoc_cookie(resp, reinterpret_cast<void**>(&results));
    results->status = lcb_respsubdoc_status(resp);

    if (results->status != LCB_SUCCESS) {
        return;
    }

    std::size_t number_of_results = lcb_respsubdoc_result_size(resp);
    results->entries.resize(number_of_results);
    for (size_t idx = 0; idx < number_of_results; ++idx) {
        results->entries[idx].status = lcb_respsubdoc_result_status(resp, idx);
        const char* buf = nullptr;
        std::size_t buf_len = 0;
        lcb_respsubdoc_result_value(resp, idx, &buf, &buf_len);
        if (buf_len > 0) {
            results->entries[idx].value.assign(buf, buf_len);
        }
    }
}

int
main()
{
    std::string username{ "Administrator" };
    std::string password{ "password" };
    std::string connection_string{ "couchbase://127.0.0.1" };
    std::string bucket_name{ "travel-sample" };

    lcb_CREATEOPTS* create_options = nullptr;
    check(lcb_createopts_create(&create_options, LCB_TYPE_BUCKET), "build options object for lcb_create");
    check(lcb_createopts_credentials(create_options, username.c_str(), username.size(), password.c_str(), password.size()),
        "assign credentials");
    check(lcb_createopts_connstr(create_options, connection_string.c_str(), connection_string.size()), "assign connection string");
    check(lcb_createopts_bucket(create_options, bucket_name.c_str(), bucket_name.size()), "assign bucket name");

    lcb_INSTANCE* instance = nullptr;
    check(lcb_create(&instance, create_options), "create lcb_INSTANCE");
    check(lcb_createopts_destroy(create_options), "destroy options object");
    check(lcb_connect(instance), "schedule connection");
    check(lcb_wait(instance, LCB_WAIT_DEFAULT), "wait for connection");
    check(lcb_get_bootstrap_status(instance), "check bootstrap status");

    std::string key{ "airline_123" };
    {
      std::string value{ R"({"country":"Iceland", "callsign":"ICEAIR", "iata":"FI", "icao":"ICE", "id":123, "name":"Icelandair", "type":"airline"})" };

      lcb_CMDSTORE* cmd = nullptr;
      check(lcb_cmdstore_create(&cmd, LCB_STORE_UPSERT), "create UPSERT command");
      check(lcb_cmdstore_key(cmd, key.c_str(), key.size()), "assign ID for UPSERT command");
      check(lcb_cmdstore_value(cmd, value.c_str(), value.size()), "assign value for UPSERT command");
      check(lcb_store(instance, nullptr, cmd), "schedule UPSERT command");
      check(lcb_cmdstore_destroy(cmd), "destroy UPSERT command");
      lcb_wait(instance, LCB_WAIT_DEFAULT);
    }

    lcb_install_callback(instance, LCB_CALLBACK_SDLOOKUP, reinterpret_cast<lcb_RESPCALLBACK>(sdget_callback));

    {
        SubdocResults results;

        lcb_SUBDOCSPECS* specs = nullptr;
        check(lcb_subdocspecs_create(&specs, 1), "create SUBDOC operations container");

        std::vector<std::string> paths{
            "name",
        };

        check(lcb_subdocspecs_get(specs, 0, 0, paths[0].c_str(), paths[0].size()), "create SUBDOC-GET operation");

        lcb_CMDSUBDOC* cmd = nullptr;
        check(lcb_cmdsubdoc_create(&cmd), "create SUBDOC command");
        check(lcb_cmdsubdoc_key(cmd, key.c_str(), key.size()), "assign ID to SUBDOC command");
        check(lcb_cmdsubdoc_specs(cmd, specs), "assign operations to SUBDOC command");
        check(lcb_subdoc(instance, &results, cmd), "schedule SUBDOC command");
        check(lcb_cmdsubdoc_destroy(cmd), "destroy SUBDOC command");
        check(lcb_subdocspecs_destroy(specs), "destroy SUBDOC operations");

        lcb_wait(instance, LCB_WAIT_DEFAULT);

        check(results.status, "status of SUBDOC operation");
        std::size_t idx = 0;
        for (const auto& entry : results.entries) {
            if (entry.status == LCB_SUCCESS) {
                std::cout << "New Document name: " << (entry.value.empty() ? "(no value)" : entry.value) << "\n";
            } else {
                std::cout << "code=" << lcb_strerror_short(entry.status) << "\n";
            }
            ++idx;
        }
    }

    lcb_destroy(instance);
    return 0;
}
