/*
File: basic-cc-query-positional-param.cc 
Description: Query w/ Positional Param

This example shows how to use positional parameters with the cluster.query() method.
<br/><br/>
Visit the docs to learn more about
<a target="_blank" href="https://docs.couchbase.com/c-sdk/current/howtos/n1ql-queries-with-sdk.html">N1QL Queries with C/C++</a>.
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

struct Rows {
    std::vector<std::string> rows{};
    std::string metadata{};
};

static void
query_callback(lcb_INSTANCE*, int, const lcb_RESPQUERY* resp)
{
    lcb_STATUS status = lcb_respquery_status(resp);
    if (status != LCB_SUCCESS) {
        const lcb_QUERY_ERROR_CONTEXT* ctx;
        lcb_respquery_error_context(resp, &ctx);

        uint32_t err_code = 0;
        lcb_errctx_query_first_error_code(ctx, &err_code);

        const char* err_msg = nullptr;
        size_t err_msg_len = 0;
        lcb_errctx_query_first_error_message(ctx, &err_msg, &err_msg_len);
        std::string error_message{};
        if (err_msg_len > 0) {
            error_message.assign(err_msg, err_msg_len);
        }

        std::cerr << "[ERROR] failed to execute query " << lcb_strerror_short(status) << ". " << error_message << " (" << err_code << ")\n";
        return;
    }

    const char* buf = nullptr;
    std::size_t buf_len = 0;
    lcb_respquery_row(resp, &buf, &buf_len);
    if (buf_len > 0) {
        Rows* result = nullptr;
        lcb_respquery_cookie(resp, reinterpret_cast<void**>(&result));
        if (lcb_respquery_is_final(resp)) {
            result->metadata.assign(buf, buf_len);
        } else {
            result->rows.emplace_back(std::string(buf, buf_len));
        }
    }
}

static void
query_type(lcb_INSTANCE* instance, const std::string& bucket_name, const std::string& type)
{
    Rows result{};

    std::string statement = "SELECT x.* FROM `" + bucket_name + R"(` x WHERE x.type=$1 LIMIT 10)";

    lcb_CMDQUERY* cmd = nullptr;
    check(lcb_cmdquery_create(&cmd), "create QUERY command");
    check(lcb_cmdquery_statement(cmd, statement.c_str(), statement.size()), "assign statement for QUERY command");
    std::string type_json = "\"" + type + "\""; // production code should use JSON encoding library
    check(lcb_cmdquery_positional_param(cmd, type_json.c_str(), type_json.size()), "add positional parameter for QUERY comand");
    // Enable using prepared (optimized) statements
    check(lcb_cmdquery_adhoc(cmd, true), "enable prepared statements for QUERY command");
    check(lcb_cmdquery_callback(cmd, query_callback), "assign callback for QUERY command");
    check(lcb_query(instance, &result, cmd), "schedule QUERY command");
    check(lcb_cmdquery_destroy(cmd), "destroy QUERY command");
    lcb_wait(instance, LCB_WAIT_DEFAULT);

    std::cout << "\n--- Query returned " << result.rows.size() << std::endl;
    for (const auto& row : result.rows) {
        std::cout << row << "\n";
    }
}

int
main(int, char**)
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

    query_type(instance, bucket_name, "hotel");
    
    lcb_destroy(instance);
}
