#!/usr/bin/python
import sys

#test data generation script
# by Phil Glaser
def main():

    return_val = 0

    # variables setup
    target_vertex_name = sys.argv[3]
    output_file_name = sys.argv[1]

    print("Initiated gql placeholder test script with arguments ", sys.argv)
    print("Target vertex is: ", target_vertex_name)
    print("output file is: ", output_file_name)

    with open(output_file_name, 'a') as the_file:
        if target_vertex_name != "principle_investigator":
            the_file.write("id\tname\n")
            idx = 0
            while idx < 20:
                idx = idx + 1
                value = target_vertex_name + " # " + '{:02d}'.format(idx)
                the_file.write("%i\t%s\n" % (idx, value))
        else:
            random_names = [ ("Ldap","User1"),
                     ("Shrestha","User2"),
                     ("Rosyara","Rosemary"),
                     ("Dreher","Umesh"),
                     ("Ulat","Kate"),
                     ("Rutkoski","Victor"),
                     ("Dreisigacker","Jessica"),
                     ("Sansaloni","Susanne"),
                     ("Singh","Carolina"),
                     ("Singh","Sukhwinder"),
                     ("Ammar","Ravi"),
                     ("Sukumaran","Karim"),
                     ("Mondal","Siva"),
                     ("Bhavani","Suchismita"),
                     ("Kumar","Sridhar"),
                     ("Crossa","Uttam"),
                     ("Burgueno","Jose"),
                     ("Braun","Juan"),
                     ("Lan","Hans"),
                     ("Reynolds","Caixia"),
                     ("Molero","Matthew"),
                     ("Sehgal","Gemma"),
                     ("Kishii","Deepmala"),
                     ("Randhawa","Masahiro"),
                     ("Vikram","Mandeep"),
                     ("Campos","Prashant"),
                     ("Banziger","Jaime"),
                     ("Govindan","Marianne"),
                     ("Singh","Velu"),
                     ("He","Pawan"),
                     ("Crespo","Xinyao"),
                     ("Basnet","Leonardo"),
                     ("Baum","Bhoja"),
                     ("Olsen","Michael"),
                     ("Puebla","Michael"),
                     ("Riis","Luis"),
                     ("Pixley","Jens"),
                     ("Kropff","Kevin"),
                     ("Juliana","Martin"),
                     ("Superuser","Philomin"),
                     ("Calaminos","GADM")
                ]
            length = len(random_names)
            header_name_first = "firstname"
            header_name_last = "lastname"
            the_file.write("id\t" + header_name_last + "\t" + header_name_first + "\n")
            idx = 0
            while idx < length:
                last_name = random_names[idx][0]
                first_name = random_names[idx][1]
                the_file.write("%i\t%s\t%s\n" % (idx, last_name,first_name))
                idx = idx + 1

    if len(sys.argv) > 0 and str(sys.argv[1]) == "fail":
        print("condition")
        return_val = 1

    sys.exit(return_val)

main()